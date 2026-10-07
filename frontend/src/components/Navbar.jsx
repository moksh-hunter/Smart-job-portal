import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Briefcase, User, LogOut, PlusCircle, LayoutDashboard, Search, Menu, X } from 'lucide-react';

const Navbar = () => {
  const { user, logout, hasRole } = useAuth();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const isCandidate = hasRole('CANDIDATE');
  const isRecruiter = hasRole('RECRUITER');
  const isAdmin = hasRole('ADMIN');

  return (
    <nav className="bg-white border-b border-slate-200 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          {/* Logo & Main Nav */}
          <div className="flex items-center space-x-8">
            <Link to="/" className="flex items-center space-x-2">
              <div className="bg-blue-600 text-white p-2 rounded-lg">
                <Briefcase className="h-5 w-5" />
              </div>
              <span className="font-bold text-xl text-slate-900 tracking-tight">SmartJob<span className="text-blue-600">Portal</span></span>
            </Link>

            <div className="hidden md:flex space-x-6">
              <Link to="/jobs" className="text-slate-600 hover:text-blue-600 font-medium transition-colors">Find Jobs</Link>
              <Link to="/companies" className="text-slate-600 hover:text-blue-600 font-medium transition-colors">Companies</Link>
              <Link to="/ats-check" className="text-slate-600 hover:text-blue-600 font-medium transition-colors">ATS Checker</Link>
            </div>
          </div>

          {/* Right Action buttons */}
          <div className="hidden md:flex items-center space-x-4">
            {user ? (
              <div className="flex items-center space-x-4">
                {isRecruiter && (
                  <Link
                    to="/post-job"
                    className="inline-flex items-center space-x-1.5 bg-blue-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-blue-700 transition"
                  >
                    <PlusCircle className="h-4 w-4" />
                    <span>Post a Job</span>
                  </Link>
                )}

                <Link
                  to={isAdmin ? "/admin" : isRecruiter ? "/recruiter/dashboard" : "/candidate/dashboard"}
                  className="inline-flex items-center space-x-1.5 text-slate-700 hover:text-blue-600 px-3 py-2 rounded-md text-sm font-medium transition"
                >
                  <LayoutDashboard className="h-4 w-4" />
                  <span>Dashboard</span>
                </Link>

                <div className="flex items-center space-x-2 pl-2 border-l border-slate-200">
                  <div className="h-8 w-8 rounded-full bg-blue-100 text-blue-700 flex items-center justify-center font-bold text-xs uppercase">
                    {user.fullName ? user.fullName.substring(0, 2) : 'U'}
                  </div>
                  <span className="text-sm font-medium text-slate-800">{user.fullName || user.username}</span>
                  <button
                    onClick={handleLogout}
                    className="p-1.5 text-slate-400 hover:text-rose-600 rounded-md transition"
                    title="Log out"
                  >
                    <LogOut className="h-4 w-4" />
                  </button>
                </div>
              </div>
            ) : (
              <div className="flex items-center space-x-3">
                <Link
                  to="/login"
                  className="text-slate-700 hover:text-blue-600 px-4 py-2 text-sm font-medium transition"
                >
                  Sign In
                </Link>
                <Link
                  to="/register"
                  className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm font-medium shadow-sm transition"
                >
                  Get Started
                </Link>
              </div>
            )}
          </div>

          {/* Mobile menu button */}
          <div className="flex md:hidden items-center">
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="p-2 rounded-md text-slate-600 hover:text-slate-900"
            >
              {mobileMenuOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <div className="md:hidden border-t border-slate-200 px-4 pt-2 pb-4 space-y-2 bg-white">
          <Link to="/jobs" onClick={() => setMobileMenuOpen(false)} className="block py-2 text-slate-700 font-medium">Find Jobs</Link>
          <Link to="/companies" onClick={() => setMobileMenuOpen(false)} className="block py-2 text-slate-700 font-medium">Companies</Link>
          <Link to="/ats-check" onClick={() => setMobileMenuOpen(false)} className="block py-2 text-slate-700 font-medium">ATS Checker</Link>
          {user ? (
            <>
              <Link to={isAdmin ? "/admin" : isRecruiter ? "/recruiter/dashboard" : "/candidate/dashboard"} onClick={() => setMobileMenuOpen(false)} className="block py-2 text-blue-600 font-medium">Dashboard</Link>
              <button onClick={() => { handleLogout(); setMobileMenuOpen(false); }} className="block w-full text-left py-2 text-rose-600 font-medium">Logout</button>
            </>
          ) : (
            <div className="pt-2 flex flex-col space-y-2">
              <Link to="/login" onClick={() => setMobileMenuOpen(false)} className="text-center py-2 text-slate-700 font-medium border border-slate-300 rounded-lg">Sign In</Link>
              <Link to="/register" onClick={() => setMobileMenuOpen(false)} className="text-center py-2 bg-blue-600 text-white font-medium rounded-lg">Get Started</Link>
            </div>
          )}
        </div>
      )}
    </nav>
  );
};

export default Navbar;
