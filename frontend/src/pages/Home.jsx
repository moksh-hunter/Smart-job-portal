import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Search, MapPin, Briefcase, Sparkles, CheckCircle2, TrendingUp, Users, Building2, ArrowRight } from 'lucide-react';
import { jobService } from '../services/api';
import JobCard from '../components/JobCard';

const Home = () => {
  const [keyword, setKeyword] = useState('');
  const [location, setLocation] = useState('');
  const [featuredJobs, setFeaturedJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchJobs = async () => {
      try {
        const response = await jobService.getJobs({ page: 0, size: 6 });
        const list = response.data?.data?.content || response.data?.data || [];
        setFeaturedJobs(list);
      } catch (err) {
        // Fallback default sample jobs if db is fresh
        setFeaturedJobs([
          {
            id: 1,
            title: 'Senior Java Backend Engineer',
            companyName: 'TechCorp Global',
            location: 'Bangalore, India (Hybrid)',
            jobType: 'FULL_TIME',
            description: 'Design and develop robust distributed microservices using Spring Boot, Kafka, and PostgreSQL with high concurrency.',
            requiredSkills: ['Java', 'Spring Boot', 'Microservices', 'PostgreSQL'],
            salaryMin: 120000,
            salaryMax: 160000,
            experienceLevel: 'SENIOR',
          },
          {
            id: 2,
            title: 'Full Stack React & Node Developer',
            companyName: 'CloudScale Inc',
            location: 'Remote',
            jobType: 'FULL_TIME',
            description: 'Build fast, accessible, enterprise web interfaces using modern React, TypeScript, and REST APIs.',
            requiredSkills: ['React', 'TypeScript', 'Node.js', 'Tailwind'],
            salaryMin: 95000,
            salaryMax: 130000,
            experienceLevel: 'MID',
          },
          {
            id: 3,
            title: 'AI / Machine Learning Engineer',
            companyName: 'NextGen AI Labs',
            location: 'San Francisco, CA (Remote)',
            jobType: 'FULL_TIME',
            description: 'Deploy advanced LLM evaluation pipelines, fine-tune models, and implement smart resume ranking algorithms.',
            requiredSkills: ['Python', 'PyTorch', 'FastAPI', 'LLMs', 'NLP'],
            salaryMin: 150000,
            salaryMax: 210000,
            experienceLevel: 'LEAD',
          }
        ]);
      } finally {
        setLoading(false);
      }
    };

    fetchJobs();
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    navigate(`/jobs?keyword=${encodeURIComponent(keyword)}&location=${encodeURIComponent(location)}`);
  };

  return (
    <div className="space-y-16 pb-16">
      {/* Hero Section */}
      <section className="bg-gradient-to-b from-blue-50/70 via-white to-white py-20 px-4 sm:px-6 lg:px-8 border-b border-slate-100">
        <div className="max-w-5xl mx-auto text-center space-y-8">
          <div className="inline-flex items-center space-x-2 bg-blue-100/80 text-blue-800 px-4 py-1.5 rounded-full text-xs font-semibold uppercase tracking-wider">
            <Sparkles className="h-4 w-4 text-blue-600" />
            <span>AI-Powered ATS Matching & Enterprise Hiring</span>
          </div>

          <h1 className="text-4xl sm:text-6xl font-extrabold text-slate-900 tracking-tight leading-tight">
            Find Your Dream Job with <br className="hidden sm:inline" />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-indigo-600">
              Intelligent ATS Resume Matching
            </span>
          </h1>

          <p className="text-lg sm:text-xl text-slate-600 max-w-3xl mx-auto leading-relaxed">
            Discover thousands of high-paying tech and corporate opportunities. Get scored instantly by our built-in Applicant Tracking System before you apply.
          </p>

          {/* Search Bar Form */}
          <form
            onSubmit={handleSearch}
            className="bg-white p-3 rounded-2xl shadow-xl border border-slate-200 max-w-4xl mx-auto flex flex-col md:flex-row items-center gap-3"
          >
            <div className="flex items-center flex-1 w-full px-3 py-2 border-b md:border-b-0 md:border-r border-slate-200">
              <Search className="h-5 w-5 text-slate-400 mr-3 flex-shrink-0" />
              <input
                type="text"
                placeholder="Job title, keywords, or skills (e.g. Java, React)..."
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
                className="w-full focus:outline-none text-slate-800 placeholder-slate-400 text-sm"
              />
            </div>

            <div className="flex items-center flex-1 w-full px-3 py-2">
              <MapPin className="h-5 w-5 text-slate-400 mr-3 flex-shrink-0" />
              <input
                type="text"
                placeholder="City, state, or 'Remote'..."
                value={location}
                onChange={(e) => setLocation(e.target.value)}
                className="w-full focus:outline-none text-slate-800 placeholder-slate-400 text-sm"
              />
            </div>

            <button
              type="submit"
              className="w-full md:w-auto bg-blue-600 hover:bg-blue-700 text-white font-medium px-8 py-3.5 rounded-xl transition shadow-md flex items-center justify-center space-x-2"
            >
              <span>Search</span>
              <ArrowRight className="h-4 w-4" />
            </button>
          </form>

          {/* Popular Search tags */}
          <div className="flex flex-wrap items-center justify-center gap-2 pt-2 text-xs text-slate-500">
            <span className="font-medium text-slate-700">Popular:</span>
            {['Java', 'Spring Boot', 'React', 'Python', 'DevOps', 'Remote', 'Data Science'].map((tag) => (
              <button
                key={tag}
                type="button"
                onClick={() => { setKeyword(tag); navigate(`/jobs?keyword=${tag}`); }}
                className="bg-slate-100 hover:bg-slate-200 text-slate-700 px-3 py-1 rounded-full transition"
              >
                {tag}
              </button>
            ))}
          </div>
        </div>
      </section>

      {/* Metrics Counter */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-2 md:grid-cols-4 gap-6 bg-blue-900 text-white rounded-2xl p-8 shadow-lg">
          <div className="text-center space-y-1">
            <div className="text-3xl sm:text-4xl font-extrabold text-blue-400">10,000+</div>
            <div className="text-xs sm:text-sm text-blue-200 font-medium">Active Jobs</div>
          </div>
          <div className="text-center space-y-1">
            <div className="text-3xl sm:text-4xl font-extrabold text-blue-400">500+</div>
            <div className="text-xs sm:text-sm text-blue-200 font-medium">Top Companies</div>
          </div>
          <div className="text-center space-y-1">
            <div className="text-3xl sm:text-4xl font-extrabold text-blue-400">95%</div>
            <div className="text-xs sm:text-sm text-blue-200 font-medium">ATS Match Precision</div>
          </div>
          <div className="text-center space-y-1">
            <div className="text-3xl sm:text-4xl font-extrabold text-blue-400">24h</div>
            <div className="text-xs sm:text-sm text-blue-200 font-medium">Interview Turnaround</div>
          </div>
        </div>
      </section>

      {/* Featured Jobs Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 space-y-8">
        <div className="flex flex-col md:flex-row md:items-end justify-between">
          <div>
            <h2 className="text-2xl sm:text-3xl font-bold text-slate-900">Featured Opportunities</h2>
            <p className="text-slate-500 text-sm mt-1">Hand-picked openings matching high market demand</p>
          </div>
          <Link
            to="/jobs"
            className="inline-flex items-center space-x-1.5 text-blue-600 hover:text-blue-700 font-semibold text-sm mt-4 md:mt-0"
          >
            <span>View All Jobs</span>
            <ArrowRight className="h-4 w-4" />
          </Link>
        </div>

        {loading ? (
          <div className="text-center py-12 text-slate-400 font-medium">Loading opportunities...</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {featuredJobs.map((job) => (
              <JobCard
                key={job.id}
                job={job}
                onApply={(j) => navigate(`/jobs/${j.id}`)}
              />
            ))}
          </div>
        )}
      </section>

      {/* Why Choose Smart Job Portal */}
      <section className="bg-slate-50 border-y border-slate-200 py-16 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="text-center max-w-3xl mx-auto mb-12 space-y-3">
            <h2 className="text-3xl font-bold text-slate-900">Built for Modern Hiring</h2>
            <p className="text-slate-600 text-sm">
              We eliminate traditional hiring friction with algorithmic candidate ranking and transparent communication.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm space-y-4">
              <div className="h-12 w-12 bg-blue-100 text-blue-600 rounded-lg flex items-center justify-center">
                <Sparkles className="h-6 w-6" />
              </div>
              <h3 className="font-semibold text-lg text-slate-900">AI ATS Scoring</h3>
              <p className="text-sm text-slate-600 leading-relaxed">
                Check how well your resume matches any job description instantly and get actionable skill improvement suggestions.
              </p>
            </div>

            <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm space-y-4">
              <div className="h-12 w-12 bg-indigo-100 text-indigo-600 rounded-lg flex items-center justify-center">
                <Users className="h-6 w-6" />
              </div>
              <h3 className="font-semibold text-lg text-slate-900">Direct Recruiter Access</h3>
              <p className="text-sm text-slate-600 leading-relaxed">
                Recruiters can review pre-ranked candidates, schedule multi-round interviews, and submit structured feedback.
              </p>
            </div>

            <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm space-y-4">
              <div className="h-12 w-12 bg-emerald-100 text-emerald-600 rounded-lg flex items-center justify-center">
                <CheckCircle2 className="h-6 w-6" />
              </div>
              <h3 className="font-semibold text-lg text-slate-900">Real-Time Status</h3>
              <p className="text-sm text-slate-600 leading-relaxed">
                Never wonder where your resume went. Track submission, review, shortlist, interview, and offer milestones in real-time.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Box */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="bg-gradient-to-r from-blue-600 to-indigo-700 rounded-3xl p-10 sm:p-14 text-white text-center sm:text-left flex flex-col sm:flex-row items-center justify-between gap-8 shadow-xl">
          <div className="space-y-3 max-w-xl">
            <h3 className="text-3xl font-extrabold">Ready to hire or get hired?</h3>
            <p className="text-blue-100 text-sm leading-relaxed">
              Create a free candidate or recruiter account in seconds and unlock automated matching.
            </p>
          </div>
          <div className="flex flex-col sm:flex-row gap-3 w-full sm:w-auto">
            <Link
              to="/register"
              className="bg-white text-blue-700 hover:bg-blue-50 font-semibold px-6 py-3 rounded-xl transition text-center shadow"
            >
              Get Started Free
            </Link>
            <Link
              to="/login"
              className="bg-blue-800/80 hover:bg-blue-800 text-white font-medium px-6 py-3 rounded-xl transition text-center border border-blue-400/30"
            >
              Sign In
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
