import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { jobService, applicationService, analyticsService } from '../services/api';
import { Briefcase, Users, PlusCircle, CheckCircle, Clock, XCircle, Search, Eye } from 'lucide-react';

const RecruiterDashboard = () => {
  const [jobs, setJobs] = useState([]);
  const [selectedJob, setSelectedJob] = useState(null);
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [stats, setStats] = useState({ totalJobs: 0, totalApplicants: 0, interviewsScheduled: 0 });

  useEffect(() => {
    const fetchRecruiterData = async () => {
      try {
        const jobsRes = await jobService.getMyJobs();
        const jobList = jobsRes.data?.data?.content || jobsRes.data?.data || [];
        setJobs(jobList);
        if (jobList.length > 0) {
          setSelectedJob(jobList[0]);
          loadJobApplications(jobList[0].id);
        }
      } catch (e) {
        // Fallback default
        setJobs([]);
      } finally {
        setLoading(false);
      }
    };

    fetchRecruiterData();
  }, []);

  const loadJobApplications = async (jobId) => {
    try {
      const res = await applicationService.getJobApplications(jobId);
      const appList = res.data?.data?.content || res.data?.data || [];
      setApplications(appList);
    } catch (e) {
      setApplications([]);
    }
  };

  const handleJobSelect = (job) => {
    setSelectedJob(job);
    loadJobApplications(job.id);
  };

  const handleUpdateStatus = async (appId, newStatus) => {
    try {
      await applicationService.updateStatus(appId, newStatus);
      // Refresh
      if (selectedJob) {
        loadJobApplications(selectedJob.id);
      }
    } catch (err) {
      alert('Failed to update application status.');
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Top Bar */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
        <div>
          <h1 className="text-2xl font-bold text-slate-900">Recruiter Control Center</h1>
          <p className="text-sm text-slate-500">Manage your postings and review ATS-ranked candidate pools</p>
        </div>
        <Link
          to="/post-job"
          className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2.5 rounded-xl font-medium text-sm transition shadow flex items-center space-x-1.5"
        >
          <PlusCircle className="h-4 w-4" />
          <span>Post New Job</span>
        </Link>
      </div>

      {/* Main Grid: Left = Jobs, Right = Applicants */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Left: Job postings */}
        <div className="bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-4">
          <h2 className="font-bold text-slate-900 text-base flex items-center justify-between">
            <span>My Postings</span>
            <span className="text-xs bg-slate-100 text-slate-600 px-2 py-0.5 rounded-full">{jobs.length}</span>
          </h2>

          {loading ? (
            <div className="text-center py-8 text-slate-400 text-sm">Loading jobs...</div>
          ) : jobs.length === 0 ? (
            <div className="text-center py-10 space-y-3">
              <Briefcase className="h-10 w-10 text-slate-300 mx-auto" />
              <p className="text-xs text-slate-500">No jobs posted yet.</p>
              <Link to="/post-job" className="text-xs text-blue-600 font-semibold hover:underline">
                Create your first job listing →
              </Link>
            </div>
          ) : (
            <div className="space-y-2">
              {jobs.map((j) => (
                <div
                  key={j.id}
                  onClick={() => handleJobSelect(j)}
                  className={`p-3.5 rounded-xl border transition cursor-pointer ${
                    selectedJob?.id === j.id
                      ? 'border-blue-500 bg-blue-50/50'
                      : 'border-slate-200 hover:border-slate-300 bg-white'
                  }`}
                >
                  <h3 className="font-semibold text-sm text-slate-900 truncate">{j.title}</h3>
                  <div className="flex justify-between items-center text-xs text-slate-500 mt-1">
                    <span>{j.location || 'Remote'}</span>
                    <span className="font-medium text-blue-600">{j.applicationsCount || 0} applicants</span>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Right: Applicants for selected job */}
        <div className="lg:col-span-2 bg-white rounded-2xl border border-slate-200 shadow-sm p-6 space-y-6">
          <div className="flex justify-between items-center border-b border-slate-100 pb-4">
            <div>
              <h2 className="font-bold text-slate-900 text-base">
                Applicants for: <span className="text-blue-600">{selectedJob ? selectedJob.title : 'Select a Job'}</span>
              </h2>
              <p className="text-xs text-slate-500 mt-0.5">Candidates pre-ranked using ATS skill matching</p>
            </div>
            <span className="text-xs font-semibold bg-blue-100 text-blue-800 px-3 py-1 rounded-full">
              {applications.length} Candidates
            </span>
          </div>

          {applications.length === 0 ? (
            <div className="text-center py-16 space-y-2">
              <Users className="h-12 w-12 text-slate-300 mx-auto" />
              <p className="text-sm font-medium text-slate-700">No applications received yet for this position.</p>
              <p className="text-xs text-slate-400">Applications will appear here automatically with their calculated ATS scores.</p>
            </div>
          ) : (
            <div className="space-y-4">
              {applications.map((app) => (
                <div key={app.id} className="p-4 rounded-xl border border-slate-200 hover:border-slate-300 transition space-y-3">
                  <div className="flex justify-between items-start">
                    <div>
                      <h4 className="font-semibold text-slate-900 text-sm">{app.candidateName || app.candidateUsername || 'Candidate'}</h4>
                      <p className="text-xs text-slate-500">{app.candidateEmail || 'No email provided'}</p>
                    </div>

                    <div className="flex items-center space-x-2">
                      {app.rankingScore && (
                        <div className="text-right">
                          <span className="inline-block px-2.5 py-0.5 rounded-full text-xs font-bold bg-blue-100 text-blue-700">
                            ATS: {Math.round(app.rankingScore)}%
                          </span>
                        </div>
                      )}
                    </div>
                  </div>

                  {app.coverLetter && (
                    <p className="text-xs text-slate-600 bg-slate-50 p-2.5 rounded-lg border border-slate-100 italic">
                      "{app.coverLetter}"
                    </p>
                  )}

                  {/* Recruiter Action Buttons */}
                  <div className="flex flex-wrap items-center justify-between pt-2 border-t border-slate-100 text-xs">
                    <span className="text-slate-500">
                      Status: <strong className="text-slate-800">{app.status || 'SUBMITTED'}</strong>
                    </span>

                    <div className="flex items-center space-x-2">
                      <button
                        onClick={() => handleUpdateStatus(app.id, 'SHORTLISTED')}
                        className="px-2.5 py-1 bg-purple-50 text-purple-700 hover:bg-purple-100 rounded-md font-medium transition"
                      >
                        Shortlist
                      </button>
                      <button
                        onClick={() => handleUpdateStatus(app.id, 'INTERVIEW_SCHEDULED')}
                        className="px-2.5 py-1 bg-emerald-50 text-emerald-700 hover:bg-emerald-100 rounded-md font-medium transition"
                      >
                        Schedule Interview
                      </button>
                      <button
                        onClick={() => handleUpdateStatus(app.id, 'REJECTED')}
                        className="px-2.5 py-1 bg-rose-50 text-rose-700 hover:bg-rose-100 rounded-md font-medium transition"
                      >
                        Reject
                      </button>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default RecruiterDashboard;
