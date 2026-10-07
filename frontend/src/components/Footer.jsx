import { Briefcase, Globe, Heart } from 'lucide-react';

const Footer = () => {
  return (
    <footer className="bg-slate-900 text-slate-400 py-12 border-t border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
          <div className="space-y-4">
            <div className="flex items-center space-x-2 text-white font-bold text-lg">
              <div className="bg-blue-600 p-1.5 rounded-lg">
                <Briefcase className="h-5 w-5 text-white" />
              </div>
              <span>SmartJob<span className="text-blue-500">Portal</span></span>
            </div>
            <p className="text-sm text-slate-400 leading-relaxed">
              Next-generation hiring platform powered by AI resume ranking, intelligent ATS matching, and seamless interview workflows.
            </p>
          </div>

          <div>
            <h4 className="text-white font-semibold text-sm mb-4 uppercase tracking-wider">For Job Seekers</h4>
            <ul className="space-y-2 text-sm">
              <li><a href="/jobs" className="hover:text-white transition">Browse Jobs</a></li>
              <li><a href="/ats-check" className="hover:text-white transition">Free ATS Resume Checker</a></li>
              <li><a href="/companies" className="hover:text-white transition">Explore Companies</a></li>
              <li><a href="/candidate/dashboard" className="hover:text-white transition">Application Status</a></li>
            </ul>
          </div>

          <div>
            <h4 className="text-white font-semibold text-sm mb-4 uppercase tracking-wider">For Employers</h4>
            <ul className="space-y-2 text-sm">
              <li><a href="/post-job" className="hover:text-white transition">Post a Job</a></li>
              <li><a href="/recruiter/dashboard" className="hover:text-white transition">Applicant Tracking System</a></li>
              <li><a href="/recruiter/dashboard" className="hover:text-white transition">Schedule Interviews</a></li>
              <li><a href="/register" className="hover:text-white transition">Create Recruiter Account</a></li>
            </ul>
          </div>

          <div>
            <h4 className="text-white font-semibold text-sm mb-4 uppercase tracking-wider">Quick Info</h4>
            <div className="text-sm space-y-2">
              <p>Backend: Spring Boot 4 & Java 21</p>
              <p>Database: H2 / MySQL</p>
              <a
                href="https://github.com/moksh-hunter/Smart-job-portal"
                target="_blank"
                rel="noreferrer"
                className="inline-flex items-center space-x-2 text-blue-400 hover:text-blue-300 transition mt-2"
              >
                <Globe className="h-4 w-4" />
                <span>GitHub Repository</span>
              </a>
            </div>
          </div>
        </div>

        <div className="pt-8 border-t border-slate-800 flex flex-col md:flex-row justify-between items-center text-xs text-slate-500">
          <p>© {new Date().getFullYear()} Smart Job Portal. All rights reserved.</p>
          <p className="flex items-center space-x-1 mt-4 md:mt-0">
            <span>Built with modern Spring Boot & React</span>
          </p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
