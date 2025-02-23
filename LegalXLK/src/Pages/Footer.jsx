import './Footer.css';
import { FaFacebook, FaLinkedin, FaTwitter, FaInstagram } from 'react-icons/fa';
import Image from '../assets/Logo-white.png';

export default function Footer() {
  return (
    <footer className="footer">
      <div className="logo">
        <img src={Image} alt="Logo" />
        <span>LegalX LK</span>
      </div>
      
      <div className="footerContent">
        <div className="column">
          <h3>About Us</h3>
          <p>
            Our platform empowers lawyers with secure, efficient, and innovative tools for case management, legal research, and streamlined workflows, enhancing productivity and client service.
          </p>
          <div className="socialIcons">
            <a href="http://www.linkedin.com/in/legalx-lk"><FaLinkedin size={20} color='white'/></a>
            <a href="https://www.instagram.com/legalx_lk?igsh=MTgwd3I3cnFyNHVrbw=="><FaInstagram size={20} color='white'/></a>
          </div>
        </div>

        <div className="column">
          <h3>Services</h3>
          <ul>
            <li>Organizing documents</li>
            <li>Task and Reminders</li>
            <li>Documents analyzations and summarizations</li>
            <li>Gazettes updates</li>
          </ul>
        </div>

        <div className="column">
          <h3>Links</h3>
          <ul>
            <li>Term of use</li>
            <li>Privacy Policy</li>
          </ul>
        </div>

        <div className="column">
          <h3>Contact Us</h3>
          <ul>
            <li>+94 70 1563 481</li>
            <li>legalxlk@gmail.com</li>
          </ul>
        </div>
      </div>

      <div className="copyright">
        Copyright ©2024 LegalX LK All Right Reserved
      </div>
    </footer>
  );
}
