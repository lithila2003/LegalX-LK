import React, { useState } from 'react';
import './Home.css';
import Footer from './Footer';
import Nav from '../Components/Nav';
import { NavLink } from 'react-router';
import { Bot, FileText, Calendar, Bell, Boxes, MessageSquare, Clock, Search } from "lucide-react";
import TeamMember from '../Components/TeamMember';

const HomePage = () => {
  const members = [
    {
      name: "Vidun Wickramasinghe",
      role: "",
      image: "/images/vidun.jpeg",
      github: "https://github.com/vidun200",
      linkedin :"https://www.linkedin.com/in/vidun-wickramasinghe-677095339/"
    },
    {
      name: "Shivanka Maddumarachchi",
      role: "",
      image: "/images/shivanka.jpeg",
      linkedin: "https://www.linkedin.com/in/shivanka-maddumarachchi-414b0b262/",
      github: "https://github.com/ShivankaVM"
    },
    {
      name: "Uthpala Wijesundara ",
      role: "",
      image: "/images/uthpala.jpeg",
      github: "https://github.com/UthpalaWijesundara",
      linkedin: "https://www.linkedin.com/in/uthpala-wijesundara"
    },
    {
      name: "Lithila Mahagedara",
      role:"",
      image: "/images/lithila.jpeg",
      github: "https://github.com/lithila2003",
      linkedin: "https://www.linkedin.com/in/lithila-mahagedara-48a53a320/"
    },
    {
      name: "Lithira wettasinghe",
      role: "",
      image: "/images/lithira.jpeg",
      github: "https://github.com/Lithira7",
      linkedin: "https://www.linkedin.com/in/lithira-wettasinghe-540220232/"
    },
    {
      name: "Thinura Jayathunga",
      role: "",
      image: "/images/thinura.jpeg",
      github: "https://github.com/Thinura-dinuth?tab=repositories",
      linkedin: "https://www.linkedin.com/in/thinu-jaya?utm_source=share&utm_campaign=share_via&utm_content=profile&utm_medium=android_app"
    },
  ];

  return (
    <div>
      <Nav />

      <section className='home--top' id='home'>
        <div>
          <h1>Sri Lanka's Premier AI-Powered Legal Platform</h1>
          <h3>Experience the future of legal services with LegalX LK's AI-assisted platform and comprehensive management tools.</h3>
          <NavLink to="/signup" className="home--top-btn"><button>Try Now</button></NavLink>
        </div>
      </section>

      <section className='home--solutions' id='features'>
        <h1>Smart Legal Solutions</h1>

        <div className='home--solutions-container'>
          <div className='home--solutions-card'>
            <Bot className='home--solutions-icon'/>
            <h3>AI Legal Assistant</h3>
            <p>24/7 AI-powered legal guidance and instant answers to your questions.</p>
          </div>

          <div className='home--solutions-card'>
            <Bell className='home--solutions-icon'/>
            <h3>Gazette Updatest</h3>
            <p>Real-time notifications for legal gazette updates and changes.</p>
          </div>

          <div className='home--solutions-card'>
            <Boxes className='home--solutions-icon'/>
            <h3>Document Management</h3>
            <p>Secure, intelligent document organization and retrieval system.</p>
          </div>

          <div className='home--solutions-card'>
            <Calendar className='home--solutions-icon'/>
            <h3>Task Management</h3>
            <p>Streamlined case and task tracking with automated reminders.</p>
          </div>
        </div>

      </section>

      <section className='home--assistant' id='chatbot'>
          <div className='home--assistant-details'>
            <h1>Meet Your AI Legal Assistant</h1>
            <p>The chatbot analyzes case details and past judgments using AI to provide tailored recommendations and insights. Lawyers can interact with it to refine strategies, generate ideas, and streamline case management efficiently.</p>

            <div className='home--assistant-try'>
              <div className='home--assistant-try-heading'>
                <MessageSquare />
                <h4>Try asking</h4>
              </div>

              <div className='home--assistant-try-commands'>
                <p>"Analyze this contract for potential risks"</p>
                <p>"Summarize recent employment law changes"</p>
              </div>
            </div>
          </div>
        

          <div className='home--assistant-chat'>
            <div>
              <div className='home-assistant-chat-heading'>
                <h3>AI Chat Interface</h3>
                <Bot />
              </div>

              <div className='home--assistant-chat-reply'>
                <p>Hello! I'm your AI legal assistant. How can I help you today?</p>
              </div>
            </div>

            <div className='home--assistant-chat-input'>
              <input type="text" placeholder='Type your legal questions...' disabled/>
              <Search />
            </div>
        </div>
      </section>

      <section className='home--management'>
          <h1>Comprehensive Legal Management</h1>

          <div className='home--management-container'>
            <div className='home--management-card'>
              <FileText />
              <h3>Smart Document Management</h3>
              <ul>
                <li>Automated document categorization</li>
                <li>Full-text search capabilities</li>
                <li>Version control and tracking</li>
                <li>Secure document sharing</li>
              </ul>
            </div>

            <div className='home--management-card'>
              <Clock />
              <h3>Task Management</h3>
              <ul>
                <li>Deadline tracking</li>
                <li>Team collaboration tools</li>
                <li>Priority management</li>
                <li>Progress monitoring</li>
              </ul>
            </div>

            <div className='home--management-card'>
              <Bell />
              <h3>Gazette Updates</h3>
              <ul>
                <li>Real-time notifications</li>
                <li>Customized alerts</li>
                <li>Historical archive access</li>
                <li>Smart filtering options</li>
              </ul>
            </div>
          </div>
      </section>

      <section className='home--team' id='aboutus'>
        <h1>Who we are</h1>
        <p>We are undergraduates transforming legal workflows with AI-powered document management, real-time gazette updates, legal research chatbots, and task management for greater efficiency.</p>

        <p>Get to know the dedicated and creative minds shaping our vision.</p>

        <div className='home--team-container'>
          {members.map((member) => {
            return <TeamMember 
              key={member.name}
              image={member.image}
              name={member.name}
              role={member.role}
              linkedin={member.linkedin}
              github={member.github}
            />
          })}
        </div>
      </section>

      <section className='home--contactus' id='contactus'>
          <h1>Contact Us</h1>

          <form className='contactus-form'>
            <p>Name</p>
            <input type="text" placeholder='Type you name here...'/>

            <p>Email</p>
            <input type="email" placeholder='Type you email here...'/>

            <p>Message</p>
            <textarea placeholder='Type you message here...'/>

            <button type='submit'>Submit</button>
          </form>
      </section>

      <Footer />
    </div>
  );
};

export default HomePage;
