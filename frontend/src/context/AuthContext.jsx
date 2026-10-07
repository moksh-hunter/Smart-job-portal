import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/api';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    const savedUser = localStorage.getItem('user');
    return savedUser ? JSON.parse(savedUser) : null;
  });
  const [token, setToken] = useState(() => localStorage.getItem('token') || null);
  const [loading, setLoading] = useState(false);

  const login = async (email, password) => {
    setLoading(true);
    try {
      const response = await authService.login({ email, password });
      const authData = response.data.data;
      
      const userData = {
        userId: authData.userId,
        username: authData.username,
        email: authData.email,
        fullName: authData.fullName,
        roles: authData.roles || [],
      };

      setUser(userData);
      setToken(authData.accessToken);
      localStorage.setItem('user', JSON.stringify(userData));
      localStorage.setItem('token', authData.accessToken);
      return { success: true, data: userData };
    } catch (error) {
      const msg = error.response?.data?.message || 'Invalid email or password';
      return { success: false, error: msg };
    } finally {
      setLoading(false);
    }
  };

  const register = async (formData, isRecruiter = false) => {
    setLoading(true);
    try {
      const registerFn = isRecruiter ? authService.registerRecruiter : authService.registerCandidate;
      const response = await registerFn(formData);
      return { success: true, data: response.data };
    } catch (error) {
      const msg = error.response?.data?.message || 'Registration failed. Please check your details.';
      return { success: false, error: msg };
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  };

  const hasRole = (role) => {
    if (!user || !user.roles) return false;
    return user.roles.includes(role) || user.roles.includes(`ROLE_${role}`);
  };

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout, hasRole }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
