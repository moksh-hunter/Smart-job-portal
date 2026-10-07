import axios from 'axios';

const API = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Attach JWT access token to every outgoing request if available
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => Promise.reject(error));

// Handle 401 Unauthorized responses
API.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // If unauthorized, clear token if expired
      // Do not redirect forcefully to allow public browsing
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: (credentials) => API.post('/auth/login', credentials),
  registerCandidate: (data) => API.post('/auth/register/candidate', data),
  registerRecruiter: (data) => API.post('/auth/register/recruiter', data),
  getCurrentUser: () => API.get('/users/me'),
};

export const jobService = {
  getJobs: (params) => API.get('/jobs', { params }),
  getJobById: (id) => API.get(`/jobs/${id}`),
  createJob: (data) => API.post('/jobs', data),
  getMyJobs: (params) => API.get('/jobs/my-jobs', { params }),
  deleteJob: (id) => API.delete(`/jobs/${id}`),
};

export const applicationService = {
  applyForJob: (jobId, data) => API.post(`/applications/jobs/${jobId}/apply`, data),
  getMyApplications: (params) => API.get('/applications/my-applications', { params }),
  getJobApplications: (jobId, params) => API.get(`/applications/jobs/${jobId}`, { params }),
  updateStatus: (id, status) => API.patch(`/applications/${id}/status`, { status }),
};

export const atsService = {
  calculateScore: (jobId) => API.get(`/ats/jobs/${jobId}/score`),
  getRanking: (jobId) => API.get(`/ats/jobs/${jobId}/ranking`),
};

export const companyService = {
  getCompanies: (params) => API.get('/companies', { params }),
  getCompanyById: (id) => API.get(`/companies/${id}`),
};

export const analyticsService = {
  getRecruiterStats: () => API.get('/analytics/recruiter/dashboard'),
  getAdminStats: () => API.get('/analytics/admin/dashboard'),
};

export default API;
