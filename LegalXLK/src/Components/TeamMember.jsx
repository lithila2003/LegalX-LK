import React from 'react';
import "./TeamMember.css";
import { LinkedinIcon, Github } from 'lucide-react';

const TeamMember = ({image, name, role, linkedin, github}) => {
  return (
    <div className='team--card'>
        <img src={image} alt="user image" />
        <h3>{name}</h3>
        <p>{role}</p>

        <div>
            <a href={linkedin}><LinkedinIcon/></a>
            <a href={github}><Github /></a>
        </div>
    </div>
  )
}

export default TeamMember