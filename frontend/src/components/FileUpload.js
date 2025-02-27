import React, { useState } from 'react';
import { uploadFollowersFile, uploadFollowingFile } from '../services/api';

const FileUpload = ({ onSuccess, onError }) => {
  const [followersFile, setFollowersFile] = useState(null);
  const [followingFile, setFollowingFile] = useState(null);
  const [followersFileName, setFollowersFileName] = useState('');
  const [followingFileName, setFollowingFileName] = useState('');
  const [uploading, setUploading] = useState(false);

  const handleFollowersChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFollowersFile(file);
      setFollowersFileName(file.name);
    }
  };

  const handleFollowingChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFollowingFile(file);
      setFollowingFileName(file.name);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!followersFile || !followingFile) {
      onError('Error: Please select both followers and following files');
      return;
    }
    
    setUploading(true);

    try {
      // First upload followers file
      await uploadFollowersFile(followersFile);
      
      // Then upload following file
      await uploadFollowingFile(followingFile);
      
      // Notify parent component of success
      onSuccess();
      
      // Reset form
      setFollowersFile(null);
      setFollowingFile(null);
      setFollowersFileName('');
      setFollowingFileName('');
    } catch (error) {
      console.error('Error uploading files:', error);
      onError(`Error: ${error.response?.data?.message || 'Failed to upload files'}`);
    } finally {
      setUploading(false);
    }
  };

  return (
    <div className="file-upload-container">
      <h2>Upload Instagram Data Files</h2>
      <form onSubmit={handleSubmit}>
        <div className="file-input-group">
          <label className="file-input-label">
            <span className="file-button">Choose Followers File</span>
            <input 
              type="file" 
              onChange={handleFollowersChange} 
              className="file-input" 
              accept=".json"
              disabled={uploading}
            />
            <span className="file-name">{followersFileName || 'No file chosen'}</span>
          </label>
        </div>
        
        <div className="file-input-group">
          <label className="file-input-label">
            <span className="file-button">Choose Following File</span>
            <input 
              type="file" 
              onChange={handleFollowingChange} 
              className="file-input" 
              accept=".json"
              disabled={uploading}
            />
            <span className="file-name">{followingFileName || 'No file chosen'}</span>
          </label>
        </div>
        
        <button 
          type="submit" 
          className="submit-button"
          disabled={!followersFile || !followingFile || uploading}
        >
          {uploading ? 'Uploading...' : 'Process Files'}
        </button>
      </form>
      
      <div className="instructions">
        <p>
          <strong>How to get your Instagram data:</strong>
        </p>
        <ol>
          <li>Go to your Instagram profile</li>
          <li>Click on Settings → Accounts Center</li>
          <li>Press on Information and permissions → Download your Information</li>
          <li>Choose 'Some of your information' and scroll down to Followers and Following</li>
          <li>Choose JSON format and request the data</li>
          <li>Once received, upload the followers.json and following.json files</li>
        </ol>
      </div>
    </div>
  );
};

export default FileUpload;