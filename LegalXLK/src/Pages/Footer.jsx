import './Footer.css';
import { FaFacebook, FaLinkedin, FaTwitter, FaInstagram } from 'react-icons/fa';
import Image from '../assets/Logo.png';

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
            Our platform empowers lawyers with secure, efficient, and innovative tools for case management, client communication, legal research, and streamlined workflows, enhancing productivity and client service.
          </p>
          <div className="socialIcons">
            <FaFacebook size={20} />
            <FaLinkedin size={20} />
            <FaTwitter size={20} />
            <FaInstagram size={20} />
          </div>
        </div>

        <div className="column">
          <h3>Services</h3>
          <ul>
            <li>Organizing documents</li>
            <li>Task and Reminders</li>
            <li>Documents analyzations and summarizations</li>
            <li>Gazettes updates</li>
            <li>Contacting clients</li>
          </ul>
        </div>

        <div className="column">
          <h3>Page</h3>
          <ul>
            <li>Lawyer</li>
            <li>Appointment</li>
            <li>Documentation</li>
            <li>Cases</li>
            <li>News</li>
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
            <li><span>+94701563481</span></li>
            <li>117 Sri Lanka</li>
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
