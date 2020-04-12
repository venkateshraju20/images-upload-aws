import React, { useState, useEffect, useCallback } from 'react';
import './App.css';
import axios from 'axios';
import { useDropzone } from 'react-dropzone';

const UserProfiles = () => {

  const [userProfiles, setUserProfiles] = useState([]);

  const fetchUserProfiles = () => {
    axios.get("http://localhost:8080/api/user-profile").then(response => {
      console.log(response.data);
      setUserProfiles(response.data);
    });
  }

  useEffect(() => {
    fetchUserProfiles();
  }, []);

  return userProfiles.map((userProfile, index) => {
    console.log("Id: ", userProfile.userProfileId);
    return (
      <div key={index}>
        {
          userProfile.userProfileId ?
            <img src={`http://localhost:8080/api/user-profile/${userProfile.userProfileId}/image/download`} />
            : null
        }
        <br />
        <br />
        <h1>{userProfile.userProfileId}</h1>
        <h1>{userProfile.userName}</h1>
        <Dropzone {...userProfile} />
      </div>
    );
  });
}

function Dropzone({ userProfileId }) {
  const onDrop = useCallback(acceptedFiles => {
    //console.log(userProfileId);
    console.log(acceptedFiles[0]);
    const file = acceptedFiles[0];

    const formData = new FormData();
    formData.append("file", file);

    axios.post(`http://localhost:8080/api/user-profile/${userProfileId}/image/upload`,
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data"
        }
      }
    ).then(() => {
      console.log("File has been uploaded to S3");
    }).catch(e => {
      console.log(e);
    });

  }, [])
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop })

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the files here ...</p> :
          <p>Drag 'n' drop some image here, or click to select image</p>
      }
    </div>
  )
}

function App() {
  return (
    <div className="App">
      <UserProfiles />
    </div>
  );
}

export default App;
