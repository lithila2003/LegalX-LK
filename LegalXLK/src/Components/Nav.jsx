import React, {useState} from 'react';
import './Nav.css';
import Logo from '../assets/Logo-white.png';
import { Link } from 'react-router-dom';

const Nav = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const scrollToSection = (sectionId) => {
    const element = document.getElementById(sectionId);
    if (element) {
      setIsMenuOpen(false);
      
      const navbarHeight = document.querySelector('.navbar').offsetHeight;
      
      const elementPosition = element.getBoundingClientRect().top;
      
      const offsetPosition = elementPosition + window.pageYOffset - navbarHeight;
  
      window.scrollTo({
        top: offsetPosition,
        behavior: 'smooth'
      });
    }
  }

  return (
    <nav className="navbar">
        <div className='navbar--logo'>
          <img src={Logo} alt="Logo" />
          <h1>LegalX LK</h1>
        </div>

        <div className="navbar--menu-icon" onClick={toggleMenu}>
          <div className={`menu-icon ${isMenuOpen ? 'open' : ''}`}>
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>

        <div className={`navbar--links ${isMenuOpen ? 'active' : ''}`}>
          <a onClick={() => scrollToSection('home')} className="navbar--link">Home</a>
          <a onClick={() => scrollToSection('features')} className="navbar--link">Features</a>
          <a onClick={() => scrollToSection('aboutus')} className="navbar--link">About Us</a>
          <a onClick={() => scrollToSection('contactus')} className="navbar--link">Contact Us</a>


          <div className='navbar--options'>
              <Link to="/signup" className="navbar--signup">Sign Up</Link>
              <Link to="/login" className="navbar--login">Login</Link>
          </div>
        </div>
    </nav>
  )
}

export default Nav

