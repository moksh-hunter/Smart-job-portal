import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { Search, MapPin, Filter, Briefcase, RefreshCw } from 'lucide-react';
import { jobService } from '../services/api';
import JobCard from '../components/JobCard';

const Jobs = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const [keyword, setKeyword] = useState(searchParams.get('keyword') || '');
  const [location, setLocation] = useState(searchParams.get('location') || '');
  const [jobType, setJobType] = useState(searchParams.get('jobType') || '');
  const [experienceLevel, setExperienceLevel] = useState(searchParams.get('experienceLevel') || '');

  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [totalElements, setTotalElements] = useState(0);

  const fetchJobs = async () => {
    setLoading(true);
    try {
      const params = {};
      if (keyword) params.keyword = keyword;
      if (location) params.location = location;
      if (jobType) params.jobType = jobType;
      if (experienceLevel) params.experienceLevel = experienceLevel;

      const response = await jobService.getJobs(params);
      const data = response.data?.data;
      if (data && data.content) {
        setJobs(data.content);
        setTotalElements(data.totalElements || data.content.length);
      } else if (Array.isArray(data)) {
        setJobs(data);
        setTotalElements(data.length);
      } else {
        setJobs([]);
        setTotalElements(0);
      }
    } catch (err) {
      setJobs([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchJobs();
  }, [searchParams]);

  const handleFilterSubmit = (e) => {
    e.preventDefault();
    const params = {};
    if (keyword) params.keyword = keyword;
    if (location) params.location = location;
    if (jobType) params.jobType = jobType;
    if (experienceLevel) params.experienceLevel = experienceLevel;
    setSearchParams(params);
  };

  const handleReset = () => {
    setKeyword('');
    setLocation('');
    setJobType('');
    setExperienceLevel('');
    setSearchParams({});
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Search Header */}
      <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
        <h1 className="text-2xl font-bold text-slate-900">Explore Open Positions</h1>
        <p className="text-sm text-slate-500">Filter through thousands of enterprise openings matching your career goals</p>

        <form onSubmit={handleFilterSubmit} className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-3 pt-2">
          <div className="relative">
            <Search className="h-4 w-4 absolute left-3 top-3.5 text-slate-400" />
            <input
              type="text"
              placeholder="Title, skill, or keyword..."
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              className="w-full pl-9 pr-3 py-2.5 bg-slate-50 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white"
            />
          </div>

          <div className="relative">
            <MapPin className="h-4 w-4 absolute left-3 top-3.5 text-slate-400" />
            <input
              type="text"
              placeholder="Location or 'Remote'..."
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              className="w-full pl-9 pr-3 py-2.5 bg-slate-50 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white"
            />
          </div>

          <div>
            <select
              value={jobType}
              onChange={(e) => setJobType(e.target.value)}
              className="w-full px-3 py-2.5 bg-slate-50 border border-slate-300 rounded-lg text-sm text-slate-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white"
            >
              <option value="">All Job Types</option>
              <option value="FULL_TIME">Full Time</option>
              <option value="PART_TIME">Part Time</option>
              <option value="CONTRACT">Contract</option>
              <option value="INTERNSHIP">Internship</option>
              <option value="REMOTE">Remote</option>
            </select>
          </div>

          <div className="flex space-x-2">
            <button
              type="submit"
              className="flex-1 bg-blue-600 hover:bg-blue-700 text-white font-medium py-2.5 px-4 rounded-lg text-sm transition shadow-sm flex items-center justify-center space-x-1"
            >
              <Filter className="h-4 w-4 mr-1" />
              <span>Apply Filters</span>
            </button>
            <button
              type="button"
              onClick={handleReset}
              className="p-2.5 border border-slate-300 text-slate-600 hover:bg-slate-100 rounded-lg transition"
              title="Reset Filters"
            >
              <RefreshCw className="h-4 w-4" />
            </button>
          </div>
        </form>
      </div>

      {/* Results Header */}
      <div className="flex justify-between items-center text-sm text-slate-600">
        <div>
          Showing <span className="font-semibold text-slate-900">{jobs.length}</span> results
        </div>
      </div>

      {/* Job Cards Grid */}
      {loading ? (
        <div className="text-center py-20 text-slate-400 font-medium">Loading opportunities...</div>
      ) : jobs.length === 0 ? (
        <div className="text-center py-20 bg-white rounded-2xl border border-dashed border-slate-300 space-y-4">
          <Briefcase className="h-12 w-12 text-slate-300 mx-auto" />
          <h3 className="text-lg font-semibold text-slate-800">No jobs found matching your criteria</h3>
          <p className="text-sm text-slate-500 max-w-sm mx-auto">Try clearing some filters or searching for more generic keywords.</p>
          <button
            onClick={handleReset}
            className="px-4 py-2 bg-blue-600 text-white rounded-lg text-sm font-medium hover:bg-blue-700"
          >
            Clear Filters
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {jobs.map((job) => (
            <JobCard
              key={job.id}
              job={job}
              onApply={(j) => navigate(`/jobs/${j.id}`)}
            />
          ))}
        </div>
      )}
    </div>
  );
};

export default Jobs;
