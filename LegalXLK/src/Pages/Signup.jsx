import React, { useState } from 'react';
import './Signup.css';
import Image from "../assets/Signup.jpeg";
import GoogleIcon from "../assets/Google.png";
import AppleIcon from "../assets/Apple.png";

const Signup = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    id: '',
    password: '',
    confirmPassword: '',
    agreed: false,
  });
  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  const validateForm = () => {
    const errors = {};
    if (!formData.name) errors.name = 'Name is required';
    if (!formData.email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      errors.email = 'Valid email is required';
    }
    if (!formData.id) errors.id = 'Lawyer ID is required';
    if (!formData.password) errors.password = 'Password is required';
    if (formData.password.length < 6) errors.password = 'Password must be at least 6 characters';
    if (formData.password !== formData.confirmPassword) {
      errors.confirmPassword = 'Passwords do not match';
    }
    if (!formData.agreed) errors.agreed = 'You must agree to the Terms & Policy';

    setErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateForm()) {
      console.log('Form Data:', formData);
    }
  };

  return (
    <div className="signup-container">
      <div className="form-section">
        <h2>Get Started Now</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              name="name"
              placeholder="Enter your name"
              value={formData.name}
              onChange={handleChange}
              aria-label="Name"
              required
            />
            {errors.name && <p className="error">{errors.name}</p>}
          </div>
          <div className="form-group">
            <input
              type="email"
              name="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={handleChange}
              aria-label="Email"
              required
            />
            {errors.email && <p className="error">{errors.email}</p>}
          </div>
          <div className="form-group">
            <input
              type="text"
              name="id"
              placeholder="Enter your Lawyer ID"
              value={formData.id}
              onChange={handleChange}
              aria-label="Lawyer ID"
              required
            />
            {errors.id && <p className="error">{errors.id}</p>}
          </div>
          <div className="form-group">
            <input
              type="password"
              name="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
              aria-label="Password"
              required
            />
            {errors.password && <p className="error">{errors.password}</p>}
          </div>
          <div className="form-group">
            <input
              type="password"
              name="confirmPassword"
              placeholder="Confirm your password"
              value={formData.confirmPassword}
              onChange={handleChange}
              aria-label="Confirm Password"
              required
            />
            {errors.confirmPassword && <p className="error">{errors.confirmPassword}</p>}
          </div>
          <div className="form-group checkbox">
            <label>
              <input
                type="checkbox"
                name="agreed"
                checked={formData.agreed}
                onChange={handleChange}
                aria-label="Agree to Terms"
                required
              />
              I agree to the Terms & Policy
            </label>
            {errors.agreed && <p className="error">{errors.agreed}</p>}
          </div>
          <button type="submit" className="signup-button">
            Signup
          </button>
        </form>

        <div className="social-login">
          <p>Or</p>
          <button className="social-btn google-btn">
            <img src={GoogleIcon} alt="Google" className="social-icon" />
            Sign up with Google
          </button>
          <button className="social-btn apple-btn">
            <img src={AppleIcon} alt="Apple" className="social-icon" />
            Sign up with Apple
          </button>
        </div>

        <p className="signin-link">
          Have an account? <a href="/signin">Sign in</a>
        </p>
      </div>

      <div className="image-section">
        <img src={Image} alt="Legal Imagery" />
      </div>
    </div>
  );
};

export default Signup;
