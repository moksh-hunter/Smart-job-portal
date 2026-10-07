import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { applicationService, atsService } from '../services/api';
import { Briefcase, CheckCircle, Clock, AlertCircle, FileText, Sparkles, TrendingUp, ChevronRight } from 'lucide-react';

const CandidateDashboard = () => {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchApplications = async () => {
      try {
        const res = await applicationService.getMyApplications();
        const list = res.data?.data?.content || res.data?.data || [];
        setApplications(list);
      } catch (e) {
        setApplications([]);
      } finally {
        setLoading(false);
      }
    };

    fetchApplications();
  }, []);

  const getStatusBadge = (status) => {
    const styles = {
      SUBMITTED: 'bg-blue-50 text-blue-700 border-blue-200',
      UNDER_REVIEW: 'bg-amber-50 text-amber-700 border-amber-200',
      SHORTLISTED: 'bg-purple-50 text-purple-700 border-purple-200',
      INTERVIEW_SCHEDULED: 'bg-emerald-50 text-emerald-700 border-emerald-200',
      OFFERED: 'bg-teal-50 text-teal-700 border-teal-200',
      REJECTED: 'bg-rose-50 text-rose-700 border-rose-200',
    };
    return (
      <span className={`px-2.5 py-1 rounded-full text-xs font-semibold border ${styles[status] || 'bg-slate-100 text-slate-700 border-slate-200'}`}>
        {status ? status.replace('_', ' ') : 'SUBMITTED'}
      </span>
    );
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Welcome Banner */}
      <div className="bg-gradient-to-r from-blue-600 to-indigo-700 rounded-2xl p-8 text-white flex flex-col md:flex-row justify-between items-start md:items-center gap-4 shadow-lg">
        <div className="space-y-2">
          <h1 className="text-2xl sm:text-3xl font-bold">Welcome, {user?.fullName || 'Candidate'}!</h1>
          <p className="text-blue-100 text-sm">Track your job applications, interview schedules, and ATS resume match rates.</p>
        </div>
        <Link
          to="/ats-check"
          className="bg-white text-blue-700 font-semibold px-4 py-2.5 rounded-xl shadow text-sm hover:bg-blue-50 transition flex items-center space-x-1.5"
        >
          <Sparkles className="h-4 w-4" />
          <span>ATS Resume Checker</span>
        </Link>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center space-x-4">
          <div className="h-12 w-12 rounded-xl bg-blue-100 text-blue-600 flex items-center justify-center">
            <Briefcase className="h-6 w-6" />
          </div>
          <div>
            <div className="text-2xl font-bold text-slate-900">{applications.length}</div>
            <div className="text-xs text-slate-500 font-medium">Total Applications</div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center space-x-4">
          <div className="h-12 w-12 rounded-xl bg-emerald-100 text-emerald-600 flex items-center justify-center">
            <CheckCircle className="h-6 w-6" />
          </div>
          <div>
            <div className="text-2xl font-bold text-slate-900">
              {applications.filter(a => a.status === 'SHORTLISTED' || a.status === 'INTERVIEW_SCHEDULED').length}
            </div>
            <div className="text-xs text-slate-500 font-medium">Shortlisted / Interviews</div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm flex items-center space-x-4">
          <div className="h-12 w-12 rounded-xl bg-indigo-100 text-indigo-600 flex items-center justify-center">
            <TrendingUp className="h-6 w-6" />
          </div>
          <div>
            <div className="text-2xl font-bold text-slate-900">88%</div>
            <div className="text-xs text-slate-500 font-medium">Avg. Profile ATS Score</div>
          </div>
        </div>
      </div>

      {/* Applications List */}
      <div className="bg-white rounded-2xl border border-slate-200 shadow-sm overflow-hidden">
        <div className="p-6 border-b border-slate-100 flex justify-between items-center">
          <h2 className="text-lg font-bold text-slate-900">My Job Applications</h2>
          <Link to="/jobs" className="text-sm font-semibold text-blue-600 hover:text-blue-700">Find More Jobs →</Link>
        </div>

        {loading ? (
          <div className="text-center py-12 text-slate-400">Loading your applications...</div>
        ) : applications.length === 0 ? (
          <div className="text-center py-16 space-y-3">
            <FileText className="h-12 w-12 text-slate-300 mx-auto" />
            <h3 className="text-base font-semibold text-slate-800">No applications submitted yet</h3>
            <p className="text-xs text-slate-500 max-w-sm mx-auto">Browse our latest job openings and apply with one click.</p>
            <Link to="/jobs" className="inline-block mt-2 px-4 py-2 bg-blue-600 text-white rounded-lg text-xs font-semibold hover:bg-blue-700">
              Explore Jobs
            </Link>
          </div>
        ) : (
          <div className="divide-y divide-slate-100">
            {applications.map((app) => (
              <div key={app.id} className="p-6 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 hover:bg-slate-50 transition">
                <div className="space-y-1">
                  <h3 className="font-semibold text-slate-900 text-base">{app.jobTitle || 'Software Engineer'}</h3>
                  <p className="text-xs text-slate-500">{app.companyName || 'Enterprise Employer'} • Applied on {new Date(app.createdAt || Date.now()).toLocaleDateString()}</p>
                </div>

                <div className="flex items-center space-x-4">
                  {getStatusBadge(app.status)}
                  {app.atsScore && (
                    <span className="text-xs font-semibold bg-blue-50 text-blue-700 px-2.5 py-1 rounded-full border border-blue-200">
                      ATS: {Math.round(app.atsScore)}%
                    </span>
                  )}
                  <Link to={`/jobs/${app.jobId}`} className="text-slate-400 hover:text-slate-600">
                    <ChevronRight className="h-5 w-5" />
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default CandidateDashboard;
