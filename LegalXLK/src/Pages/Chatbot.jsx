import React, { useState } from 'react';
import Nav from '../Components/Nav';
import './Chatbot.css';

const Chatbot = () => {
  const [messages, setMessages] = useState([]);
  const [input, setInput] = useState('');
  const [loading, setLoading] = useState(false);

  const sendMessage = async () => {
    if (!input.trim()) return;

    const newMessages = [...messages, { sender: 'user', text: input }];
    setMessages(newMessages);
    setInput('');
    setLoading(true);

    try {
      const response = await fetch('http://localhost:5000/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: input }),
      });

      if (!response.ok) throw new Error('Failed to fetch response from AI.');

      const data = await response.json();
      setMessages([...newMessages, { sender: 'ai', text: data.response }]);
    } catch (error) {
      console.error('Error communicating with the AI:', error);
      setMessages([...newMessages, { sender: 'ai', text: 'Error: Could not fetch response.' }]);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <Nav />
    <div className="chatbot-layout">
      <div className="sidebar">
        <h3>Old Chats</h3>
        <ul>
          {messages
            .filter((msg) => msg.sender === 'user')
            .map((msg, index) => (
              <li key={index}>{msg.text}</li>
            ))}
        </ul>
      </div>
      <div className="chat-container">
        <div className="chatbot-header">
          <h2>Case AI</h2>
          <p>How can I help you today?</p>
        </div>
        <div className="chat-window">
          {messages.map((msg, index) => (
            <div
              key={index}
              className={`message ${msg.sender === 'user' ? 'user' : 'ai'}`}
            >
              {msg.text}
            </div>
          ))}
          {loading && <div className="loading-message">Thinking...</div>}
        </div>
        <div className="input-container">
          <input
            className='chat--input'
            type="text"
            placeholder="Type your question here..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
          <button className='btn' onClick={sendMessage}>Send</button>
        </div>
      </div>
    </div>
    </>
  );
};
export default Chatbot;
