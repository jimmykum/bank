# Bank Application

<p> Technology </p>
<ul>
  <li>Spring Webflux.</li>
  <li>MongoDb with Mongodb Reactive client</li>
</ul>

<p> Usage Guide: </p>
<ul>
  <>Start mongodb using docker compose . <br/> Docker compose file is provided at project root.
     docker-entrypoint-initdb.d folder contains script for create default database user name & password.<br/>
     These user name & password db name are configured in application.yml <br/>
     cmd to start mongodb : docker-compose up 
  </li>
  <li> Integration test uses the docker mongodb . (Embedded mongodb is not used)</li>
</ul>

  Running the application on windows. <br/>
  A script is provided which will start the docker compose service and will run the spring boot application also. <br/>
  Usage :  D:\pro2022\pismo> .\run.bat
   


   
