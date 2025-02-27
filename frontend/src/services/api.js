import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

export const getNonFollowingUsers = async () => {
  try {
    const response = await api.get('/api/not-following-back');
    return response.data;
  } catch (error) {
    console.error('Error fetching non-following users:', error);
    throw error;
  }
};

// Upload followers file
export const uploadFollowersFile = async (file) => {
  const formData = new FormData();
  formData.append('followers', file);
  
  try {
    const response = await api.post('/uploadFollowers', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error uploading followers file:', error);
    throw error;
  }
};

// Upload following file
export const uploadFollowingFile = async (file) => {
  const formData = new FormData();
  formData.append('following', file);
  
  try {
    const response = await api.post('/uploadFollowing', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
    return response.data;
  } catch (error) {
    console.error('Error uploading following file:', error);
    throw error;
  }
};

// Upload JSON data directly
export const uploadJsonText = async (jsonFollowers, jsonFollowing) => {
  try {
    const response = await api.post('/uploadStringFormat', {
      jsonFollowers,
      jsonFollowing
    });
    return response.data;
  } catch (error) {
    console.error('Error uploading JSON text:', error);
    throw error;
  }
};

export default {
  getNonFollowingUsers,
  uploadFollowersFile,
  uploadFollowingFile,
  uploadJsonText
};