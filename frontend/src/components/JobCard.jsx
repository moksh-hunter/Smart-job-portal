import React from 'react';
import { Link } from 'react-router-dom';
import { MapPin, Briefcase, DollarSign, Clock, Building } from 'lucide-react';

const JobCard = ({ job, onApply }) => {
  const formatSalary = (min, max) => {
    if (!min && !max) return 'Competitive';
    if (min && max) return `$${(min / 1000).toFixed(0)}k - $${(max / 1000).toFixed(0)}k / yr`;
    if (min) return `From $${(min / 1000).toFixed(0)}k / yr`;
    return `Up to $${(max / 1000).toFixed(0)}k / yr`;
  };

  return (
    <div className="bg-white rounded-xl border border-slate-200 p-6 hover:shadow-md transition-shadow flex flex-col justify-between group">
      <div>
        <div className="flex items-start justify-between mb-4">
          <div className="flex items-center space-x-3">
            <div className="w-12 h-12 rounded-lg bg-blue-50 border border-blue-100 flex items-center justify-center text-blue-600 font-bold text-lg">
              {job.companyName ? job.companyName.charAt(0) : 'J'}
            </div>
            <div>
              <h3 className="font-semibold text-lg text-slate-900 group-hover:text-blue-600 transition">
                {job.title}
              </h3>
              <p className="text-sm text-slate-500 flex items-center space-x-1">
                <Building className="h-3.5 w-3.5 mr-1" />
                {job.companyName || 'Verified Enterprise'}
              </p>
            </div>
          </div>
          <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-50 text-emerald-700 border border-emerald-200">
            {job.jobType ? job.jobType.replace('_', ' ') : 'Full Time'}
          </span>
        </div>

        <p className="text-sm text-slate-600 line-clamp-2 mb-4 leading-relaxed">
          {job.description}
        </p>

        {/* Required skills badges */}
        {job.requiredSkills && (
          <div className="flex flex-wrap gap-1.5 mb-4">
            {(Array.isArray(job.requiredSkills) ? job.requiredSkills : job.requiredSkills.split(',')).slice(0, 4).map((skill, index) => (
              <span key={index} className="bg-slate-100 text-slate-700 text-xs px-2.5 py-1 rounded-md font-medium">
                {skill.trim()}
              </span>
            ))}
          </div>
        )}

        {/* Job metadata */}
        <div className="grid grid-cols-2 gap-2 text-xs text-slate-500 mb-4 pt-3 border-t border-slate-100">
          <div className="flex items-center space-x-1">
            <MapPin className="h-3.5 w-3.5 text-slate-400" />
            <span className="truncate">{job.location || 'Remote'}</span>
          </div>
          <div className="flex items-center space-x-1">
            <DollarSign className="h-3.5 w-3.5 text-slate-400" />
            <span>{formatSalary(job.salaryMin, job.salaryMax)}</span>
          </div>
          <div className="flex items-center space-x-1">
            <Briefcase className="h-3.5 w-3.5 text-slate-400" />
            <span>{job.experienceLevel || 'Entry to Senior'}</span>
          </div>
          <div className="flex items-center space-x-1">
            <Clock className="h-3.5 w-3.5 text-slate-400" />
            <span>Active</span>
          </div>
        </div>
      </div>

      <div className="flex items-center space-x-3 pt-2">
        <Link
          to={`/jobs/${job.id}`}
          className="flex-1 text-center py-2 px-3 border border-slate-300 rounded-lg text-sm font-medium text-slate-700 hover:bg-slate-50 transition"
        >
          Details
        </Link>
        <button
          onClick={() => onApply && onApply(job)}
          className="flex-1 py-2 px-3 bg-blue-600 hover:bg-blue-700 text-white rounded-lg text-sm font-medium transition shadow-sm"
        >
          Quick Apply
        </button>
      </div>
    </div>
  );
};

export default JobCard;
