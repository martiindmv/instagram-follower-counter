import React, { useState } from 'react';
import { uploadJsonText } from '../services/api';

const JsonUpload = ({ onSuccess, onError }) => {
  const [jsonFollowers, setJsonFollowers] = useState('');
  const [jsonFollowing, setJsonFollowing] = useState('');
  const [uploading, setUploading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!jsonFollowers || !jsonFollowing) {
      onError('Error: Please paste both followers and following JSON data');
      return;
    }

    try {
      // Validate JSON 
      try {
        JSON.parse(jsonFollowers);
        JSON.parse(jsonFollowing);
      } catch (e) {
        onError('Error: Invalid JSON format. Please check your data.');
        return;
      }

      setUploading(true);

      // Send data to backend
      await uploadJsonText(jsonFollowers, jsonFollowing);
      
      onSuccess();
      
      setJsonFollowers('');
      setJsonFollowing('');
    } catch (error) {
      console.error('Error uploading JSON:', error);
      onError(`Error: ${error.response?.data?.message || 'Failed to process JSON data'}`);
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="json-upload-container">
      <h2>Paste Instagram JSON Data</h2>
      <form onSubmit={handleSubmit}>
        <div className="textarea-group">
          <label htmlFor="jsonFollowers">Followers JSON:</label>
          <textarea
            id="jsonFollowers"
            value={jsonFollowers}
            onChange={(e) => setJsonFollowers(e.target.value)}
            placeholder="Paste your followers JSON data here..."
            rows={5}
            disabled={uploading}
          />
        </div>
        
        <div className="textarea-group">
          <label htmlFor="jsonFollowing">Following JSON:</label>
          <textarea
            id="jsonFollowing"
            value={jsonFollowing}
            onChange={(e) => setJsonFollowing(e.target.value)}
            placeholder="Paste your following JSON data here..."
            rows={5}
            disabled={uploading}
          />
        </div>
        
        <button 
          type="submit" 
          className="submit-button"
          disabled={!jsonFollowers || !jsonFollowing || uploading}
        >
          {uploading ? 'Processing...' : 'Process JSON'}
        </button>
      </form>
    </div>
  );
};

export default JsonUpload;