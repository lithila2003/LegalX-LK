import React, { useState } from 'react';
import './Signup.css'; // Import CSS for styling
import Image from "../assets/Signup.jpeg"
import GoogleIcon from "../assets/Google.png";  // Google Icon
import AppleIcon from "../assets/Apple.png";  // Apple Icon

const Signup = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    agreed: false,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form Data:', formData);
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
              required
            />
          </div>
          <div className="form-group">
            <input
              type="email"
              name="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="text"
              name="id"
              placeholder="Enter your Lawyer ID "
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <input
              type="password"
              name="password"
              placeholder="Enter your password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group checkbox">
            <label>
              <input
                type="checkbox"
                name="agreed"
                checked={formData.agreed}
                onChange={handleChange}
                required
              />
              I agree to the Terms & policy
            </label>
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
