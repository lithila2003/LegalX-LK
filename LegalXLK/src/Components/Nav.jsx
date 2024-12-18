import React from 'react';
import './Nav.css';
import Image from '../assets/Logo.png';

const Nav = () => {
  return (
         <nav className="navbar">
          <div className='navbar-left'>
            <img src={Image} alt="Logo" />
            <h1 className="logo">LegalX LK</h1>
          </div>
            <ul>
              <li><a href="#home" className="active">Home</a></li>
              <li><a href="#calendar">Calendar</a></li>
              <li><a href="#services">Services</a></li>
              <li><a href="#page">Page</a></li>
              <li><a href="#logout">Log Out</a></li>
            </ul>
          </nav>
  )
}

export default Nav

