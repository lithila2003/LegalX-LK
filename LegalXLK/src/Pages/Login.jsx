import React from 'react';
import './Login.css';
import Image from "../assets/Login.jpeg"
import GoogleIcon from "../assets/Google.png";  // Google Icon
import AppleIcon from "../assets/Apple.png";  // Apple Icon


const LoginPage = () => {
  return (
    <div className="login-container">
      <div className="form-section">
        <h1>Welcome back!</h1>
        <p>Enter your Credentials to access your account</p>

        <form>
          <input type="email" placeholder="Email address" required />
          <input type="password" placeholder="Password" required />
          <div className="extras">
            <label>
              <input type="checkbox" /> Remember
            </label>
            <a href="#" className="forgot-password">Forgot password?</a>
          </div>
          <button type="submit" className="login-button">Login</button>
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

        <p className="signup-link">
          Don't have an account? <a href="#">Sign Up</a>
        </p>
      </div>
      
      <div className="image-section">
        <img src={Image} alt="Legal Imagery" />
      </div>
    </div>
    
  );
};

export default LoginPage;
