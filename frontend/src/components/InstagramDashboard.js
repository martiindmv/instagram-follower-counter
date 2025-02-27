import React from 'react';

const InstagramDashboard = ({ stats }) => {
  // If no stats are available yet, display a placeholder
  if (!stats || Object.keys(stats).length === 0) {
    return (
      <div className="dashboard-placeholder">
        <h2>Instagram Account Statistics</h2>
        <p>Upload your data to see your follower and following statistics</p>
      </div>
    );
  }

  // Calculate additional metrics
  const mutualFollows = stats.following - stats.notFollowingBack;
  const followRatio = stats.followers > 0 ? (stats.followers / stats.following).toFixed(2) : '0.00';
  const engagementScore = stats.following > 0 ? ((mutualFollows / stats.following) * 100).toFixed(1) : '0.0';
  
  return (
    <div className="dashboard-container">
      <h2>Instagram Account Statistics</h2>
      
      <div className="stats-cards">
        <div className="stat-card followers">
          <span className="stat-value">{stats.followers}</span>
          <span className="stat-label">Followers</span>
        </div>
        
        <div className="stat-card following">
          <span className="stat-value">{stats.following}</span>
          <span className="stat-label">Following</span>
        </div>
        
        <div className="stat-card not-following-back">
          <span className="stat-value">{stats.notFollowingBack}</span>
          <span className="stat-label">Not Following You</span>
        </div>
        
        <div className="stat-card mutual-follows">
          <span className="stat-value">{mutualFollows}</span>
          <span className="stat-label">Mutual Follows</span>
        </div>
      </div>
      
      <div className="visualization-container">
        <h3>Relationship Visualization</h3>
        
        <div className="relationship-bars">
          <div className="bar-container">
            <div 
              className="followers-bar" 
              style={{ 
                width: `${stats.followers > 0 ? (stats.followers / Math.max(stats.followers, stats.following) * 100) : 0}%` 
              }}
            ></div>
            <div 
              className="following-bar" 
              style={{ 
                width: `${stats.following > 0 ? (stats.following / Math.max(stats.followers, stats.following) * 100) : 0}%` 
              }}
            ></div>
            <div 
              className="mutual-bar" 
              style={{ 
                width: `${mutualFollows > 0 ? (mutualFollows / Math.max(stats.followers, stats.following) * 100) : 0}%` 
              }}
            ></div>
          </div>
        </div>
        
        <div className="legend">
          <div className="legend-item">
            <span className="color-box followers-color"></span>
            <span>Followers</span>
          </div>
          <div className="legend-item">
            <span className="color-box following-color"></span>
            <span>Following</span>
          </div>
          <div className="legend-item">
            <span className="color-box mutual-color"></span>
            <span>Mutual</span>
          </div>
        </div>
      </div>
      
      <div className="ratio-stats">
        <div className="ratio-card">
          <h4>Follow Ratio</h4>
          <div className="ratio-value">{followRatio}</div>
          <div className="ratio-explanation">A ratio > 1 means more people follow you than you follow</div>
        </div>
        
        <div className="ratio-card">
          <h4>Engagement Score</h4>
          <div className="ratio-value">{engagementScore}%</div>
          <div className="ratio-explanation">Percentage of people you follow who follow you back</div>
        </div>
      </div>
    </div>
  );
};

export default InstagramDashboard;