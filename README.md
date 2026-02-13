# About
FilmFlix application allows users to subscribe to movies available in the library. Other available features are: 
<ul>
<li>Users can be added by providing given name, email ID, and password. Note that the password is stored encrypted.</li>
<li>A user is uniquely identified by the email ID.</li>
<li>Movies can be added to the library by providing the movie name, movie description, director's name etc.</li>
<li>A movie is uniquely identified by the movie name.</li>
<li>Users can subscribe to one or more movies.</li>
<li>User details, and movie details can be retrieved.</li>
<li>Users, or movies can be deleted.</li>
</ul>

# How to build the artifact 
Run the command 
    ./gradlew build

# How to run the application  
Run the command 
    java -jar .\build\libs\filmflix-1.0.0.jar

# Swagger UI page 
Accessible from http://&lt;hostname&gt;:8081/filmflix/swagger-ui/index.html

# Functionality and REST API details  
Add A User 
<pre>POST /filmflix/users </pre> with Request body as
<pre>{
    "givenName":"John Doe",
    "email":"johndoe@gmail.com",
    "password":"changeit"
}</pre>

Get All Users' Details <pre>GET /filmflix/users</pre>
Get A User's Details <pre>GET /filmflix/users/&lt;userEmailID&gt;</pre>
Delete A User <pre>DELETE /filmflix/users/&lt;userEmailID&gt;</pre>

Add Multiple Movies <pre>POST /filmflix/movies</pre> with Request body as
<pre>[
    {    
        "name":"Ramayana",
        "description":"Ramayan Epic",
        "language":"English",
        "director":"John Doe",
        "genre":"Mythology",
        "releaseYear":2024,
        "durationMinutes":180,
        "rating":9.8
    },
    {    
        "name":"Mowgli",
        "description":"Adventures of Mowgli",
        "language":"English",
        "director":"John Smith",
        "genre":"Fiction",
        "releaseYear":2004,
        "durationMinutes":110,
        "rating":8
    }
]</pre>

Get All Movies' details <pre>GET /filmflix/movies</pre>
Get A Movie's Details <pre>GET /filmflix/movies/&lt;movieName&gt;</pre>
Delete Multiple Movies <pre>DELETE /filmflix/movies</pre> with Request body as 
<pre>[
    {
        "name":"Mowgli"
    }
]</pre>

Option for a user to subscribe To Multiple Movies <pre>POST /filmflix/users/&lt;userEmailID&gt;/subscribe</pre> with Request body as
<pre> [
    {    
        "name":"Mowgli"
    },
    {
        "name":"Ramayana"
    }
]</pre>