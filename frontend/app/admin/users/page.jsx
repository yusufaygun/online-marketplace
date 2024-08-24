'use client';

import React, { useEffect, useState } from 'react';
import axios from 'axios';
import useAdminRoute from '@/app/hooks/useAdminRoute';

const BACKEND_URL = 'http://localhost:8080';

const ManageUsers = () => {
  useAdminRoute(); // Admin kontrolü burada 
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [editingUser, setEditingUser] = useState(null);
  const [newUser, setNewUser] = useState({ username: '', name: '', surname: '', password: '' });
  const [newAdmin, setNewAdmin] = useState({ username: '', name: '', surname: '', password: '' });
  const [searchQuery, setSearchQuery] = useState('');
  const [showNotification, setShowNotification] = useState(false);

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const fetchUsers = async () => {
        try {
            const response = await axios.get(`${BACKEND_URL}/admin/users?page=${page}&size=10`, {
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`,
                },
            });
            const allUsers = response.data.data.content;
            const totalPages = response.data.data.totalPages;

            setUsers(allUsers);
            setFilteredUsers(allUsers);
            setTotalPages(totalPages);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    fetchUsers();
}, [page]);

  const addUser = async () => {
    try {
      const response = await axios.post(`${BACKEND_URL}/admin/users`, newUser, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      setUsers([...users, response.data]);
      setFilteredUsers([...filteredUsers, response.data]);
      setNewUser({ username: '', name: '', surname: '', password: '' });
      setShowNotification(true);
      setTimeout(() => setShowNotification(false), 3000);
      window.location.reload();
    } catch (error) {
      console.error('Error adding user:', error);
    }
  };

  const addAdmin = async () => {
    try {
      const response = await axios.post(`${BACKEND_URL}/admin/admins`, newAdmin, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      setUsers([...users, response.data]);
      setFilteredUsers([...filteredUsers, response.data]);
      setNewAdmin({ username: '', name: '', surname: '', password: '' });
      setShowNotification(true);
      setTimeout(() => setShowNotification(false), 3000);
      window.location.reload();
    } catch (error) {
      console.error('Error adding admin:', error);
    }
  };

  const editUser = async (userId) => {
    try {
      const response = await axios.put(`${BACKEND_URL}/admin/users/${userId}`, editingUser, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      setUsers(users.map(user => user.id === userId ? response.data : user));
      setFilteredUsers(filteredUsers.map(user => user.id === userId ? response.data : user));
      setEditingUser(null);
    } catch (error) {
      console.error('Error editing user:', error);
    }
  };

  const deleteUser = async (id) => {
    try {
      await axios.delete(`${BACKEND_URL}/admin/users/${id}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`,
        },
      });
      setUsers(users.filter(user => user.id !== id));
      setFilteredUsers(filteredUsers.filter(user => user.id !== id));
    } catch (error) {
      console.error('Error deleting user:', error);
    }
  };

  const handleSearch = () => {
    const lowercasedQuery = searchQuery.toLowerCase();
    const filtered = users.filter(user =>
      user.username.toLowerCase().includes(lowercasedQuery) ||
      user.name.toLowerCase().includes(lowercasedQuery) ||
      user.surname.toLowerCase().includes(lowercasedQuery)
    );
    setFilteredUsers(filtered);
  };

  const handleNextPage = () => {
    if (page < totalPages - 1) {
        setPage(prevPage => prevPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (page > 0) {
        setPage(prevPage => prevPage - 1);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-r from-teal-300 to-cyan-500 flex flex-col items-center py-10">
      <div className="container mx-auto px-6 py-10 bg-white rounded-lg shadow-lg">
        <h1 className="text-4xl font-bold mb-6 text-gray-800">Manage Users</h1>
        
        {/* Search Section */}
        <div className="flex items-center mb-6 space-x-4">
          <input
            type="text"
            placeholder="Search Users"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
          />
          <button
            onClick={handleSearch}
            className="bg-cyan-600 text-white px-4 py-2 rounded-lg shadow-md hover:bg-cyan-700"
          >
            Search
          </button>
        </div>
  
        {/* Add New User */}
        <div className="mb-6">
          <h2 className="text-2xl font-semibold mb-4 text-gray-700">Add New User</h2>
          <div className="space-y-4">
            <input
              type="text"
              placeholder="Username"
              value={newUser.username}
              onChange={(e) => setNewUser({ ...newUser, username: e.target.value })}
              className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
            />
            <input
              type="text"
              placeholder="Name"
              value={newUser.name}
              onChange={(e) => setNewUser({ ...newUser, name: e.target.value })}
              className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
            />
            <input
              type="text"
              placeholder="Surname"
              value={newUser.surname}
              onChange={(e) => setNewUser({ ...newUser, surname: e.target.value })}
              className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
            />
            <input
              type="password"
              placeholder="Password"
              value={newUser.password}
              onChange={(e) => setNewUser({ ...newUser, password: e.target.value })}
              className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
            />
            <button
              onClick={addUser}
              className="bg-cyan-600 text-white px-4 py-2 rounded-lg shadow-md hover:bg-cyan-700"
            >
              Add User
            </button>
          </div>
        </div>
  
        {/* Add New Admin */}
        <div className="mb-6">
          <h2 className="text-2xl font-semibold mb-4 text-gray-700">Add New Admin</h2>
          <div className="space-y-4">
            <input
              type="text"
              placeholder="Username"
              value={newAdmin.username}
              onChange={(e) => setNewAdmin({ ...newAdmin, username: e.target.value })}
              className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <input
              type="text"
              placeholder="Name"
              value={newAdmin.name}
              onChange={(e) => setNewAdmin({ ...newAdmin, name: e.target.value })}
              className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <input
              type="text"
              placeholder="Surname"
              value={newAdmin.surname}
              onChange={(e) => setNewAdmin({ ...newAdmin, surname: e.target.value })}
              className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <input
              type="password"
              placeholder="Password"
              value={newAdmin.password}
              onChange={(e) => setNewAdmin({ ...newAdmin, password: e.target.value })}
              className="border border-gray-300 p-3 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-green-500"
            />
            <button
              onClick={addAdmin}
              className="bg-green-600 text-white px-4 py-2 rounded-lg shadow-md hover:bg-green-700"
            >
              Add Admin
            </button>
          </div>
        </div>
  
        {/* User Table */}
        <div className="overflow-x-auto">
          <table className="min-w-full bg-white border border-gray-300 rounded-lg shadow-md">
            <thead className="bg-gray-200 text-gray-600">
              <tr>
                <th className="px-6 py-3 text-left">Username</th>
                <th className="px-6 py-3 text-left">Name</th>
                <th className="px-6 py-3 text-left">Surname</th>
                <th className="px-6 py-3 text-left">Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.map(user => (
                <tr key={user.id} className="border-b border-gray-200 hover:bg-gray-50">
                  <td className="px-6 py-4">{user.username}</td>
                  <td className="px-6 py-4">{user.name}</td>
                  <td className="px-6 py-4">{user.surname}</td>
                  <td className="px-6 py-4 flex space-x-2">
                    <button
                      onClick={() => setEditingUser(user)}
                      className="bg-yellow-500 text-white px-3 py-1 rounded-lg hover:bg-yellow-600"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => deleteUser(user.id)}
                      className="bg-red-600 text-white px-3 py-1 rounded-lg hover:bg-red-700"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
  
        {/* Pagination */}
        <div className="flex justify-between items-center mt-6">
          <button
            onClick={handlePreviousPage}
            className="bg-gray-300 text-gray-700 px-4 py-2 rounded-lg shadow-md hover:bg-gray-400"
            disabled={page === 0}
          >
            Previous
          </button>
          <span className="text-gray-600">Page {page + 1} of {totalPages}</span>
          <button
            onClick={handleNextPage}
            className="bg-gray-300 text-gray-700 px-4 py-2 rounded-lg shadow-md hover:bg-gray-400"
            disabled={page === totalPages - 1}
          >
            Next
          </button>
        </div>
  
        {/* Edit User Modal */}
        {editingUser && (
          <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
            <div className="bg-white p-6 rounded-lg shadow-lg w-1/3">
              <h2 className="text-2xl font-semibold mb-4">Edit User</h2>
              <input
                type="text"
                placeholder="Username"
                value={editingUser.username}
                onChange={(e) => setEditingUser({ ...editingUser, username: e.target.value })}
                className="border border-gray-300 p-3 mb-4 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
              />
              <input
                type="text"
                placeholder="Name"
                value={editingUser.name}
                onChange={(e) => setEditingUser({ ...editingUser, name: e.target.value })}
                className="border border-gray-300 p-3 mb-4 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
              />
              <input
                type="text"
                placeholder="Surname"
                value={editingUser.surname}
                onChange={(e) => setEditingUser({ ...editingUser, surname: e.target.value })}
                className="border border-gray-300 p-3 mb-4 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-cyan-500"
              />
              <button
                onClick={() => editUser(editingUser.id)}
                className="bg-cyan-600 text-white px-4 py-2 rounded-lg shadow-md hover:bg-cyan-700"
              >
                Save Changes
              </button>
              <button
                onClick={() => setEditingUser(null)}
                className="ml-4 bg-gray-300 text-gray-700 px-4 py-2 rounded-lg shadow-md hover:bg-gray-400"
              >
                Cancel
              </button>
            </div>
          </div>
        )}
  
        {/* Notification */}
        {showNotification && (
          <div className="fixed bottom-0 right-0 m-4 bg-green-600 text-white px-4 py-2 rounded-lg shadow-lg">
            User added successfully!
          </div>
        )}
      </div>
    </div>
  );
  
};

export default ManageUsers;

          
