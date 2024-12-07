Enter In Browser http://localhost:8080/book-store

Browser ( client ) created HTTP request and sent to server
    GET http://localhost:8080/book-store

Tomcat ( server )
    Received GET http://localhost:8080/book-store
    Tomcat does not see any call to specific URL
    Tomcat looks into web.xml file into <welcome-file-list>
    Tomcat did ot find index.html or index.htm under "apache-tomcat-9.0.93\webapps\book-store" directory
    Tomcat found index.jsp under "apache-tomcat-9.0.93\webapps\book-store" directory
    Tomcat forwards HTTP request to Tomcat's JSP container ( JSP engine ).
    JSP container reads index.jsp line by line ( including header.jsp, footer.jsp ) and converts index.jsp into index_jsp.java ( index_jsp.class ) servlet
    JSP container passes control to Tomcat's Servlet container
    Servlet container executes service method of index_jsp.java servlet
        Call to DB to get genre names ( SELECT genre_name from genres; )
        Execute Java code to create and populate HTML with headers values
        Call to DB to select all books
        Execute Java code to create and populate HTML with books
    At the end of index_jsp.java servlet execution we will have fully created HTML page
    Tomcat will send this fully created HTML page to Browser ( to client )

Browser ( client )
    Received HTML page in response from Tomcat server
    Reads HTML response line by line what means that Browser executes HTML page to draw the page
    When Browser see <img src="./book_images/HobbitCover.png" it means that Browser does call to Tomcat server to get the HobbitCover.png picture
    Browser creates new HTTP request to call GET http://localhost:8080/book-store/book_images/HobbitCover.png and send

Tomcat ( server )
    Received GET http://localhost:8080/book-store/book_images/HobbitCover.png
    Tomcat tries to find the file HobbitCover.png under book_images directory so under "apache-tomcat-9.0.93\webapps\book-store\book_images" directory
    Tomcat found the HobbitCover.png under book_images directory and Tomcat sends this picture to Browser and Browser can display it now in appropriate place.
