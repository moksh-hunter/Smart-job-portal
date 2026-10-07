import React, { useState, useEffect } from 'react';
import { analyticsService } from '../services/api';
import { Users, Briefcase, FileCheck, Shield, Database, Activity, CheckCircle2 } from 'lucide-react';

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    totalUsers: 24,
    totalJobs: 12,
    totalApplications: 45,
    activeInterviews: 8,
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div className="bg-slate-900 rounded-3xl p-8 text-white space-y-3 shadow-xl">
        <div className="inline-flex items-center space-x-2 bg-blue-500/20 text-blue-300 px-3 py-1 rounded-full text-xs font-semibold">
          <Shield className="h-4 w-4" />
          <span>System Administration</span>
        </div>
        <h1 className="text-3xl font-extrabold tracking-tight">Enterprise Admin Console</h1>
        <p className="text-slate-400 text-sm max-w-xl">
          High-level operational metrics, user accounts, and platform health monitoring.
        </p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-2">
          <div className="flex justify-between items-center text-slate-400">
            <span className="text-xs font-semibold uppercase tracking-wider">Registered Users</span>
            <Users className="h-5 w-5 text-blue-600" />
          </div>
          <div className="text-3xl font-bold text-slate-900">{stats.totalUsers}</div>
          <p className="text-xs text-emerald-600 font-medium">↑ Active & Verified</p>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-2">
          <div className="flex justify-between items-center text-slate-400">
            <span className="text-xs font-semibold uppercase tracking-wider">Active Jobs</span>
            <Briefcase className="h-5 w-5 text-indigo-600" />
          </div>
          <div className="text-3xl font-bold text-slate-900">{stats.totalJobs}</div>
          <p className="text-xs text-blue-600 font-medium">Across verified employers</p>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-2">
          <div className="flex justify-between items-center text-slate-400">
            <span className="text-xs font-semibold uppercase tracking-wider">Applications Processed</span>
            <FileCheck className="h-5 w-5 text-emerald-600" />
          </div>
          <div className="text-3xl font-bold text-slate-900">{stats.totalApplications}</div>
          <p className="text-xs text-slate-500">Ranked via ATS scoring</p>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-2">
          <div className="flex justify-between items-center text-slate-400">
            <span className="text-xs font-semibold uppercase tracking-wider">Server Status</span>
            <Activity className="h-5 w-5 text-emerald-600" />
          </div>
          <div className="text-3xl font-bold text-emerald-600 flex items-center space-x-1">
            <span>UP</span>
            <CheckCircle2 className="h-5 w-5" />
          </div>
          <p className="text-xs text-slate-500">Port 8080 (Tomcat 11)</p>
        </div>
      </div>

      {/* Database & Quick Links */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
          <h3 className="font-bold text-slate-900 text-base flex items-center space-x-2">
            <Database className="h-5 w-5 text-blue-600" />
            <span>Database & Storage</span>
          </h3>
          <p className="text-sm text-slate-600">
            Active persistence layer running with H2 in-memory storage (dev profile) or MySQL.
          </p>
          <a
            href="http://localhost:8080/h2-console"
            target="_blank"
            rel="noreferrer"
            className="inline-block px-4 py-2 bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-semibold rounded-lg transition"
          >
            Open H2 Console (JDBC: jdbc:h2:mem:smart_job_portal) →
          </a>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm space-y-4">
          <h3 className="font-bold text-slate-900 text-base flex items-center space-x-2">
            <Activity className="h-5 w-5 text-emerald-600" />
            <span>Interactive API Documentation</span>
          </h3>
          <p className="text-sm text-slate-600">
            Access and test all 11 enterprise REST controllers directly via Swagger UI.
          </p>
          <a
            href="http://localhost:8080/swagger-ui/index.html"
            target="_blank"
            rel="noreferrer"
            className="inline-block px-4 py-2 bg-blue-50 hover:bg-blue-100 text-blue-700 text-xs font-semibold rounded-lg transition"
          >
            Launch Swagger UI →
          </a>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
