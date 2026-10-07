import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { jobService, applicationService, atsService } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Building, MapPin, DollarSign, Calendar, Briefcase, CheckCircle, ArrowLeft, Send, Sparkles, AlertCircle } from 'lucide-react';

const JobDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user, hasRole } = useAuth();

  const [job, setJob] = useState(null);
  const [loading, setLoading] = useState(true);
  const [atsScore, setAtsScore] = useState(null);
  const [applying, setApplying] = useState(false);
  const [applied, setApplied] = useState(false);
  const [coverLetter, setCoverLetter] = useState('');
  const [errorMsg, setErrorMsg] = useState('');
  const [showApplyModal, setShowApplyModal] = useState(false);

  useEffect(() => {
    const fetchJob = async () => {
      try {
        const response = await jobService.getJobById(id);
        setJob(response.data?.data);

        // If candidate is logged in, try checking ATS score for this job
        if (user && hasRole('CANDIDATE')) {
          try {
            const atsRes = await atsService.calculateScore(id);
            setAtsScore(atsRes.data?.data);
          } catch (e) {
            // ATS score may not be calculated yet if candidate profile not set
          }
        }
      } catch (err) {
        setErrorMsg('Job not found or has been closed.');
      } finally {
        setLoading(false);
      }
    };

    fetchJob();
  }, [id, user]);

  const handleApply = async (e) => {
    e.preventDefault();
    if (!user) {
      navigate('/login');
      return;
    }

    setApplying(true);
    setErrorMsg('');
    try {
      await applicationService.applyForJob(id, { coverLetter });
      setApplied(true);
      setShowApplyModal(false);
    } catch (err) {
      setErrorMsg(err.response?.data?.message || 'Failed to submit application. You may have already applied.');
    } finally {
      setApplying(false);
    }
  };

  if (loading) {
    return <div className="text-center py-20 text-slate-500">Loading job details...</div>;
  }

  if (!job) {
    return (
      <div className="max-w-3xl mx-auto py-20 px-4 text-center">
        <h2 className="text-xl font-bold text-slate-800">Job Not Found</h2>
        <p className="text-slate-500 mt-2">The job posting you are looking for might have expired or been removed.</p>
        <Link to="/jobs" className="mt-4 inline-block text-blue-600 font-medium">← Back to all jobs</Link>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <Link to="/jobs" className="inline-flex items-center space-x-2 text-sm text-slate-600 hover:text-blue-600 transition">
        <ArrowLeft className="h-4 w-4" />
        <span>Back to listings</span>
      </Link>

      {/* Header Card */}
      <div className="bg-white rounded-2xl border border-slate-200 p-8 shadow-sm flex flex-col md:flex-row justify-between items-start md:items-center gap-6">
        <div className="space-y-3">
          <div className="flex items-center space-x-3">
            <div className="w-16 h-16 rounded-xl bg-blue-50 border border-blue-100 flex items-center justify-center text-blue-600 font-bold text-2xl">
              {job.companyName ? job.companyName.charAt(0) : 'J'}
            </div>
            <div>
              <h1 className="text-2xl sm:text-3xl font-bold text-slate-900">{job.title}</h1>
              <p className="text-slate-600 font-medium flex items-center mt-1">
                <Building className="h-4 w-4 mr-1 text-slate-400" />
                {job.companyName || 'Verified Enterprise'}
              </p>
            </div>
          </div>

          <div className="flex flex-wrap gap-4 text-sm text-slate-500 pt-2">
            <span className="flex items-center"><MapPin className="h-4 w-4 mr-1 text-slate-400" />{job.location || 'Remote'}</span>
            <span className="flex items-center"><DollarSign className="h-4 w-4 mr-1 text-slate-400" />{job.salaryMin ? `$${job.salaryMin.toLocaleString()} - $${job.salaryMax?.toLocaleString()} / yr` : 'Competitive'}</span>
            <span className="flex items-center"><Briefcase className="h-4 w-4 mr-1 text-slate-400" />{job.jobType?.replace('_', ' ')}</span>
          </div>
        </div>

        <div className="w-full md:w-auto">
          {applied ? (
            <div className="flex items-center space-x-2 bg-emerald-50 text-emerald-700 px-6 py-3 rounded-xl font-medium border border-emerald-200">
              <CheckCircle className="h-5 w-5" />
              <span>Application Submitted!</span>
            </div>
          ) : (
            <button
              onClick={() => setShowApplyModal(true)}
              className="w-full md:w-auto bg-blue-600 hover:bg-blue-700 text-white font-medium px-8 py-3.5 rounded-xl shadow-md transition flex items-center justify-center space-x-2"
            >
              <Send className="h-4 w-4" />
              <span>Apply Now</span>
            </button>
          )}
        </div>
      </div>

      {/* ATS Match Card (If logged in) */}
      {atsScore && (
        <div className="bg-gradient-to-r from-blue-50 to-indigo-50 border border-blue-200 rounded-2xl p-6 flex flex-col sm:flex-row items-center justify-between gap-4">
          <div className="flex items-center space-x-4">
            <div className="h-12 w-12 rounded-xl bg-blue-600 text-white flex items-center justify-center font-bold text-lg">
              {atsScore.overallScore ? `${Math.round(atsScore.overallScore)}%` : 'ATS'}
            </div>
            <div>
              <h4 className="font-semibold text-slate-900 flex items-center space-x-2">
                <span>ATS Resume Match Score</span>
                <Sparkles className="h-4 w-4 text-amber-500" />
              </h4>
              <p className="text-xs text-slate-600">Calculated based on your registered skills, experience, and job requirements.</p>
            </div>
          </div>
          <Link to="/ats-check" className="text-blue-600 font-semibold text-sm hover:underline">
            View detailed ATS breakdown →
          </Link>
        </div>
      )}

      {/* Main Content */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div className="md:col-span-2 space-y-8 bg-white p-8 rounded-2xl border border-slate-200 shadow-sm">
          <div>
            <h3 className="text-lg font-bold text-slate-900 mb-3">Job Description</h3>
            <div className="text-slate-700 text-sm leading-relaxed whitespace-pre-line">
              {job.description}
            </div>
          </div>

          {job.requirements && (
            <div className="pt-6 border-t border-slate-100">
              <h3 className="text-lg font-bold text-slate-900 mb-3">Requirements</h3>
              <div className="text-slate-700 text-sm leading-relaxed whitespace-pre-line">
                {job.requirements}
              </div>
            </div>
          )}

          {job.requiredSkills && (
            <div className="pt-6 border-t border-slate-100">
              <h3 className="text-lg font-bold text-slate-900 mb-3">Required Technical Skills</h3>
              <div className="flex flex-wrap gap-2">
                {(Array.isArray(job.requiredSkills) ? job.requiredSkills : job.requiredSkills.split(',')).map((skill, index) => (
                  <span key={index} className="bg-blue-50 text-blue-700 text-xs px-3 py-1.5 rounded-lg font-semibold border border-blue-100">
                    {skill.trim()}
                  </span>
                ))}
              </div>
            </div>
          )}
        </div>

        {/* Sidebar Info */}
        <div className="space-y-6">
          <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
            <h3 className="font-bold text-slate-900 text-sm uppercase tracking-wider">Job Overview</h3>
            <div className="space-y-3 text-sm text-slate-600">
              <div>
                <span className="text-slate-400 block text-xs">Experience Level</span>
                <span className="font-medium text-slate-800">{job.experienceLevel || 'Mid - Senior'}</span>
              </div>
              <div>
                <span className="text-slate-400 block text-xs">Job Type</span>
                <span className="font-medium text-slate-800">{job.jobType?.replace('_', ' ')}</span>
              </div>
              <div>
                <span className="text-slate-400 block text-xs">Location</span>
                <span className="font-medium text-slate-800">{job.location || 'Remote'}</span>
              </div>
              <div>
                <span className="text-slate-400 block text-xs">Total Applicants</span>
                <span className="font-medium text-slate-800">{job.applicationsCount || 0} applied</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Apply Modal */}
      {showApplyModal && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl max-w-lg w-full p-6 shadow-2xl space-y-4 border border-slate-200">
            <h3 className="text-xl font-bold text-slate-900">Apply for {job.title}</h3>
            <p className="text-sm text-slate-500">Your candidate profile and resume will be submitted to the hiring team.</p>

            {errorMsg && (
              <div className="p-3 bg-rose-50 text-rose-700 text-xs rounded-lg flex items-center space-x-2">
                <AlertCircle className="h-4 w-4 flex-shrink-0" />
                <span>{errorMsg}</span>
              </div>
            )}

            <form onSubmit={handleApply} className="space-y-4">
              <div>
                <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
                  Cover Letter / Note to Recruiter (Optional)
                </label>
                <textarea
                  rows="4"
                  placeholder="Introduce yourself and explain why you're a great fit for this role..."
                  value={coverLetter}
                  onChange={(e) => setCoverLetter(e.target.value)}
                  className="w-full p-3 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
              </div>

              <div className="flex justify-end space-x-3 pt-2">
                <button
                  type="button"
                  onClick={() => setShowApplyModal(false)}
                  className="px-4 py-2 border border-slate-300 rounded-lg text-sm font-medium text-slate-700 hover:bg-slate-50"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={applying}
                  className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-sm font-medium transition shadow disabled:opacity-50"
                >
                  {applying ? 'Submitting...' : 'Confirm Application'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default JobDetails;
