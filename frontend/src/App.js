import React, { useState, useEffect } from 'react';
import './App.css';
import Header from './components/Header';
import FileUpload from './components/FileUpload';
import InstagramTable from './components/InstagramTable';
import JsonUpload from './components/JsonUpload';
import { getNonFollowingUsers } from './services/api';

function App() {
  const [nonFollowingUsers, setNonFollowingUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState('');
  
  useEffect(() => {
    fetchData();
  }, []);
  
  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await getNonFollowingUsers();
      console.log('API Response:', response); // Debug response
      
      if (response && response.difference) {
        setNonFollowingUsers(response.difference);
      } else {
        console.warn('API response missing expected data format:', response);
      }
      setError(null);
    } catch (err) {
      console.error('Error fetching data:', err);
      setError('Failed to load data. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  
  const handleUploadSuccess = () => {
    setMessage('Processing your data...');
    setLoading(true);
    
    setTimeout(() => {
      fetchData();
      setMessage('Analysis complete!');
      
      setTimeout(() => {
        setMessage('');
      }, 3000);
    }, 1500);
  };
  
  const handleError = (errorMsg) => {
    setError(errorMsg);
    setLoading(false);
    
    setTimeout(() => {
      setError('');
    }, 5000);
  };

  return (
    <div className="app-container">
      <Header />
      
      <div className="upload-section">
        <FileUpload 
          onSuccess={handleUploadSuccess} 
          onError={handleError}
        />
        
        <div className="or-divider">
          <span>OR</span>
        </div>
        
        <JsonUpload 
          onSuccess={handleUploadSuccess} 
          onError={handleError}
        />
      </div>
      
      {message && (
        <div className={`message ${message.includes('Error') ? 'error' : 'success'}`}>
          {message}
        </div>
      )}
      
      {error && (
        <div className="message error">
          {error}
        </div>
      )}
      
      {loading ? (
        <div className="loading-spinner">
          <div className="spinner"></div>
          <p>Processing your Instagram data...</p>
        </div>
      ) : (
        <InstagramTable nonFollowingUsers={nonFollowingUsers} />
      )}
    </div>
  );
}

export default App;