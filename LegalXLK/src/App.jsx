import React from 'react'
import Home from './Pages/Home';
import Login from './Pages/Login';
import Signup from './Pages/Signup';
import Chatbot from './Pages/Chatbot';
import { BrowserRouter, Routes, Route } from 'react-router-dom';


const App = () => {
  return (
    <div>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Home/>}/>
          <Route path='/login' element={<Login />} />
          <Route path='/signup' element={<Signup />}/>
          <Route path='/chatbot' element={<Chatbot/>}/>
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App

