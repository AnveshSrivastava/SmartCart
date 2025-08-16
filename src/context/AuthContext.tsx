import React, { createContext, useContext, useEffect, useState, ReactNode } from 'react';
import { User, AuthContextType } from '../types';
import { authAPI } from '../services/api';
import { toast } from 'sonner';
import { jwtDecode } from 'jwt-decode';

interface JWTPayload {
  sub: string;
  userId: string;
  role: string;
  exp: number;
  iat: number;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const initializeAuth = async () => {
      const token = localStorage.getItem('authToken');
      
      if (token) {
        try {
          const decoded = jwtDecode<JWTPayload>(token);
          
          // Check if token is expired
          if (decoded.exp * 1000 < Date.now()) {
            localStorage.removeItem('authToken');
            localStorage.removeItem('user');
            setIsLoading(false);
            return;
          }

          // Validate token with backend
          try {
            await authAPI.validateToken();
            const savedUser = localStorage.getItem('user');
            if (savedUser) {
              setUser(JSON.parse(savedUser));
            }
          } catch (error) {
            console.error('Token validation failed:', error);
            localStorage.removeItem('authToken');
            localStorage.removeItem('user');
          }
        } catch (error) {
          console.error('Error parsing JWT token:', error);
          localStorage.removeItem('authToken');
          localStorage.removeItem('user');
        }
      }
      setIsLoading(false);
    };

    initializeAuth();
  }, []);

  const login = async (email: string, password: string) => {
    try {
      setIsLoading(true);
      const response = await authAPI.login(email, password);
      
      if (response.success) {
        localStorage.setItem('authToken', response.token);
        
        const user: User = {
          id: response.userId,
          email: response.email,
          fullName: response.fullName,
          role: response.role as 'USER' | 'ADMIN'
        };
        
        localStorage.setItem('user', JSON.stringify(user));
        setUser(user);
        
        toast.success('Login successful!');
      } else {
        throw new Error(response.message || 'Login failed');
      }
      
    } catch (error) {
      console.error('Login error:', error);
      const errorMessage = error?.response?.data?.message || error?.message || 'Invalid credentials. Please try again.';
      toast.error(errorMessage);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const register = async (email: string, password: string, name: string) => {
    try {
      setIsLoading(true);
      const response = await authAPI.register(email, password, name);
      
      if (response.success) {
        localStorage.setItem('authToken', response.token);
        
        const user: User = {
          id: response.userId,
          email: response.email,
          fullName: response.fullName,
          role: response.role as 'USER' | 'ADMIN'
        };
        
        localStorage.setItem('user', JSON.stringify(user));
        setUser(user);
        
        toast.success('Registration successful!');
      } else {
        throw new Error(response.message || 'Registration failed');
      }
      
    } catch (error) {
      console.error('Registration error:', error);
      const errorMessage = error?.response?.data?.message || error?.message || 'Registration failed. Please try again.';
      toast.error(errorMessage);
      throw error;
    } finally {
      setIsLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
    setUser(null);
    toast.success('Logged out successfully!');
  };

  const value: AuthContextType = {
    user,
    login,
    register,
    logout,
    isLoading,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};