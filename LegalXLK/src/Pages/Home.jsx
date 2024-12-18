import React from 'react';
import './Home.css';
import Image from '../assets/Logo.png';
import Image1 from '../assets/Organize Documents.jpeg';
import Image2 from '../assets/Task and Reminders.jpeg';
import Image3 from '../assets/Document Analyzation.jpeg';
import Image4 from '../assets/Gazette Updates.jpg';
import Image5 from '../assets/Contacting Clients.jpeg';
import Footer from './Footer';
import Nav from '../Components/Nav';

const HomePage = () => {
  return (
    <div className="homepage">
      <Nav />

      {/* Section 1 - Organize Documents */}
      <section className="section section-documents">
        <div className="text-content">
          <h2>Organize <span className="highlight">Documents</span></h2>
          <button className="btn">Get started</button>
        </div>
        <div className="image-content">
          <img src={Image1} alt="Documents management" />
        </div>
      </section>

      {/* Section 2 - Task and Reminders */}
      <section className="section section-tasks">
        <div className="image-content">
          <img src={Image2} alt="Gavel and Calendar" />
        </div>
        <div className="text-content">
          <h2>Task And <span className="highlight">Reminders</span></h2>
          <button className="btn">Add Events</button>
        </div>
      </section>

      {/* Section 3 - Document Analysis */}
      <section className="section section-analysis">
        <div className="text-content">
          <h2>Document Analyzation And <span className="highlight">Summarization</span></h2>
          <button className="btn">Get Started</button>
        </div>
        <div className="image-content">
          <img src={Image3} alt="Document Analysis" />
        </div>
      </section>

      {/* Section 4 - Gazettes Updates */}
      <section className="section section-gazzets">
        <div className="image-content">
          <img src={Image4} alt="Gazette Updates" />
        </div>
        <div className="text-content">
          <h2>Gazettes <span className="highlight">Updates</span></h2>
          <button className="btn">Be Updated</button>
        </div>
      </section>

      {/* Section 5 - Contacting Clients */}
      <section className="section section-clients">
        <div className="text-content">
          <h2>Contacting <span className="highlight">Clients</span></h2>
          <button className="btn">Contact</button>
        </div>

        <div className="image-content">
          <img src={Image5} alt="Contacting Clients" />
        </div>
      </section>

      {/* Footer */}
      <Footer />
    </div>
  );
};

export default HomePage;
