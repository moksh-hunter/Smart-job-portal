import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Briefcase, User, Mail, Lock, Phone, AlertCircle, CheckCircle } from 'lucide-react';

const Register = () => {
  const [role, setRole] = useState('CANDIDATE'); // CANDIDATE or RECRUITER
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    fullName: '',
    phoneNumber: '',
  });

  const [error, setError] = useState('');
  const [success, setSuccess] = useState(false);
  const { register, loading } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    const res = await register(formData, role === 'RECRUITER');
    if (res.success) {
      setSuccess(true);
      setTimeout(() => {
        navigate('/login');
      }, 2500);
    } else {
      setError(res.error);
    }
  };

  return (
    <div className="min-h-[85vh] flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-6 bg-white p-8 rounded-2xl border border-slate-200 shadow-xl">
        <div className="text-center space-y-2">
          <div className="inline-flex bg-blue-600 text-white p-3 rounded-xl shadow-md">
            <Briefcase className="h-6 w-6" />
          </div>
          <h2 className="text-2xl font-bold text-slate-900">Create Your Account</h2>
          <p className="text-sm text-slate-500">Join Smart Job Portal and get discovered by top companies</p>
        </div>

        {/* Role Toggle Switch */}
        <div className="grid grid-cols-2 gap-2 bg-slate-100 p-1 rounded-xl">
          <button
            type="button"
            onClick={() => setRole('CANDIDATE')}
            className={`py-2 text-sm font-semibold rounded-lg transition ${
              role === 'CANDIDATE'
                ? 'bg-white text-blue-600 shadow-sm'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Job Seeker
          </button>
          <button
            type="button"
            onClick={() => setRole('RECRUITER')}
            className={`py-2 text-sm font-semibold rounded-lg transition ${
              role === 'RECRUITER'
                ? 'bg-white text-blue-600 shadow-sm'
                : 'text-slate-600 hover:text-slate-900'
            }`}
          >
            Employer / Recruiter
          </button>
        </div>

        {error && (
          <div className="bg-rose-50 border border-rose-200 text-rose-700 px-4 py-3 rounded-xl text-sm flex items-center space-x-2">
            <AlertCircle className="h-5 w-5 flex-shrink-0" />
            <span>{error}</span>
          </div>
        )}

        {success && (
          <div className="bg-emerald-50 border border-emerald-200 text-emerald-700 px-4 py-3 rounded-xl text-sm flex items-center space-x-2">
            <CheckCircle className="h-5 w-5 flex-shrink-0" />
            <span>Account created successfully! Redirecting to login...</span>
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-3.5">
          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Full Name
            </label>
            <div className="relative">
              <User className="h-4 w-4 absolute left-3 top-3 text-slate-400" />
              <input
                type="text"
                name="fullName"
                required
                value={formData.fullName}
                onChange={handleChange}
                placeholder="John Doe"
                className="w-full pl-9 pr-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Username
            </label>
            <div className="relative">
              <User className="h-4 w-4 absolute left-3 top-3 text-slate-400" />
              <input
                type="text"
                name="username"
                required
                value={formData.username}
                onChange={handleChange}
                placeholder="johndoe"
                className="w-full pl-9 pr-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Email Address
            </label>
            <div className="relative">
              <Mail className="h-4 w-4 absolute left-3 top-3 text-slate-400" />
              <input
                type="email"
                name="email"
                required
                value={formData.email}
                onChange={handleChange}
                placeholder="john@example.com"
                className="w-full pl-9 pr-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Password (min 8 chars, 1 digit, 1 special)
            </label>
            <div className="relative">
              <Lock className="h-4 w-4 absolute left-3 top-3 text-slate-400" />
              <input
                type="password"
                name="password"
                required
                value={formData.password}
                onChange={handleChange}
                placeholder="Password@123"
                className="w-full pl-9 pr-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-700 uppercase tracking-wider mb-1">
              Phone Number (Optional)
            </label>
            <div className="relative">
              <Phone className="h-4 w-4 absolute left-3 top-3 text-slate-400" />
              <input
                type="text"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                placeholder="9876543210"
                className="w-full pl-9 pr-3 py-2 bg-slate-50 border border-slate-300 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={loading || success}
            className="w-full py-2.5 mt-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-xl shadow-md transition disabled:opacity-50 text-sm"
          >
            {loading ? 'Creating Account...' : `Register as ${role === 'CANDIDATE' ? 'Job Seeker' : 'Recruiter'}`}
          </button>
        </form>

        <div className="text-center text-sm text-slate-500 pt-2 border-t border-slate-100">
          Already registered?{' '}
          <Link to="/login" className="text-blue-600 font-semibold hover:underline">
            Sign In here
          </Link>
        </div>
      </div>
    </div>
  );
};

export default Register;
