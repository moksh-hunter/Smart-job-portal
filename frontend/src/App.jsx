import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';

import Home from './pages/Home';
import Jobs from './pages/Jobs';
import JobDetails from './pages/JobDetails';
import Login from './pages/Login';
import Register from './pages/Register';
import CandidateDashboard from './pages/CandidateDashboard';
import RecruiterDashboard from './pages/RecruiterDashboard';
import PostJob from './pages/PostJob';
import ATSResumeChecker from './pages/ATSResumeChecker';
import AdminDashboard from './pages/AdminDashboard';

// Protected Route Guard
const ProtectedRoute = ({ children, allowedRoles }) => {
  const { user } = useAuth();
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  if (allowedRoles) {
    const hasRole = allowedRoles.some(
      r => user.roles?.includes(r) || user.roles?.includes(`ROLE_${r}`)
    );
    if (!hasRole) {
      return <Navigate to="/" replace />;
    }
  }
  return children;
};

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="min-h-screen flex flex-col bg-slate-50 text-slate-900">
          <Navbar />
          <main className="flex-1">
            <Routes>
              {/* Public Routes */}
              <Route path="/" element={<Home />} />
              <Route path="/jobs" element={<Jobs />} />
              <Route path="/jobs/:id" element={<JobDetails />} />
              <Route path="/ats-check" element={<ATSResumeChecker />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/companies" element={<Jobs />} />

              {/* Protected Candidate Routes */}
              <Route
                path="/candidate/dashboard"
                element={
                  <ProtectedRoute allowedRoles={['CANDIDATE', 'ADMIN']}>
                    <CandidateDashboard />
                  </ProtectedRoute>
                }
              />

              {/* Protected Recruiter Routes */}
              <Route
                path="/recruiter/dashboard"
                element={
                  <ProtectedRoute allowedRoles={['RECRUITER', 'ADMIN']}>
                    <RecruiterDashboard />
                  </ProtectedRoute>
                }
              />
              <Route
                path="/post-job"
                element={
                  <ProtectedRoute allowedRoles={['RECRUITER', 'ADMIN']}>
                    <PostJob />
                  </ProtectedRoute>
                }
              />

              {/* Protected Admin Routes */}
              <Route
                path="/admin"
                element={
                  <ProtectedRoute allowedRoles={['ADMIN']}>
                    <AdminDashboard />
                  </ProtectedRoute>
                }
              />

              {/* Fallback */}
              <Route path="*" element={<Navigate to="/" replace />} />
            </Routes>
          </main>
          <Footer />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
