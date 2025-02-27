import React, { useState } from 'react';

const InstagramTable = ({ nonFollowingUsers = [] }) => {
  const [searchTerm, setSearchTerm] = useState('');
  const [sortOrder, setSortOrder] = useState('asc');

  // Handle search functionality & sorting
  const filteredUsers = nonFollowingUsers.filter(user => 
    user.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const sortedUsers = [...filteredUsers].sort((a, b) => {
    return sortOrder === 'asc' 
      ? a.toLowerCase().localeCompare(b.toLowerCase())
      : b.toLowerCase().localeCompare(a.toLowerCase());
  });

  const toggleSortOrder = () => {
    setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
  };

  if (nonFollowingUsers.length === 0) {
    return (
      <div className="empty-state">
        <h2>No Results Yet</h2>
        <p>Upload your Instagram data to see who doesn't follow you back.</p>
      </div>
    );
  }

  return (
    <div className="table-container">
      <div className="table-header">
        <h2>Users Who Don't Follow You Back ({nonFollowingUsers.length})</h2>
        <div className="table-controls">
          <div className="search-box">
            <input
              type="text"
              placeholder="Search users..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
          <button onClick={toggleSortOrder} className="sort-button">
            Sort {sortOrder === 'asc' ? '↓' : '↑'}
          </button>
        </div>
      </div>

      {sortedUsers.length > 0 ? (
        <table className="instagram-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Username</th>
              <th>Profile Link</th>
            </tr>
          </thead>
          <tbody>
            {sortedUsers.map((user, index) => (
              <tr key={user}>
                <td>{index + 1}</td>
                <td>{user}</td>
                <td>
                  <a 
                    href={`https://instagram.com/${user}`} 
                    target="_blank" 
                    rel="noopener noreferrer"
                  >
                    View Profile
                  </a>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <div className="no-results">
          No users found matching "{searchTerm}"
        </div>
      )}
    </div>
  );
};

export default InstagramTable;