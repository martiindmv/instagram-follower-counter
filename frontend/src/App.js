import React, { useState, useEffect } from 'react';
import './App.css';
import Header from './components/Header';
import FileUpload from './components/FileUpload';
import InstagramTable from './components/InstagramTable';
import JsonUpload from './components/JsonUpload';
import InstagramDashboard from './components/InstagramDashboard';
import { getNonFollowingUsers, getAccountStats } from './services/api';

function App() {
  const [nonFollowingUsers, setNonFollowingUsers] = useState([]);
  const [accountStats, setAccountStats] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [message, setMessage] = useState('');
  
  // Fetch data when component mounts
  useEffect(() => {
    fetchData();
  }, []);
  
  const fetchData = async () => {
    try {
      setLoading(true);
      
      // Fetch non-following users
      const usersResponse = await getNonFollowingUsers();
      console.log('Non-following users API response:', usersResponse);
      
      if (usersResponse && usersResponse.difference) {
        setNonFollowingUsers(usersResponse.difference);
      }
      
      // Fetch account statistics
      const statsResponse = await getAccountStats();
      console.log('Account stats API response:', statsResponse);
      
      if (statsResponse) {
        setAccountStats(statsResponse);
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
    
    // Wait briefly then fetch updated results
    setTimeout(() => {
      fetchData();
      setMessage('Analysis complete!');
      
      // Clear success message after 3 seconds
      setTimeout(() => {
        setMessage('');
      }, 3000);
    }, 1500);
  };
  
  const handleError = (errorMsg) => {
    setError(errorMsg);
    setLoading(false);
    
    // Clear error message after 5 seconds
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
        <>
          <InstagramDashboard stats={accountStats} />
          <InstagramTable nonFollowingUsers={nonFollowingUsers} />
        </>
      )}
    </div>
  );
}

export default App;