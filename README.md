# Text Fingerprint

## Running Application 
For running the application use `gradle bootRun` to start backend and `yarn install` then `yarn start` for frontend.

## Testing 
Code is covered by unit tests. To run them use `gradle test`.

## How to use fingerprint service

There are 2 ways how to use the application. 

#####Send backend post request using curl or postman.
Here are given curl post request examples

a) Create fingerprint

`curl --location --request POST 'http://localhost:8080/fingerprint?text=testString' --header 'Content-Type: text/plain' --data-binary '@scratch.txt' -o book.txt`

Returns byte array with modified file.

b) Extract fingerprint from file

`curl http://localhost:8080/identify --data-binary @book.txt`

Returns dto with encoded text.
 
#####Use frontend

In scope of the task I have done an extremely simple frontend to simplify service usage.
On the frontend side only fingerprint creation functionality is implemented.
Run frontend application, insert text you wish to encode, select file and press "Get modified file".
The modified file will be downloaded to your device. 
There is implemented only one part of functionality because we cannot be sure displaying encoded text on the web browser 
will look identically same as in a simple text editor.


## Questions

#####What are the limitations on the input data in your implementation?

We have several limitations on the input data in this kind of solution. First of all, t
he maximum length of POST method URL is about 2KB. Also not every possible character can be presented in the 
url as request parameter. There are some unsafe characters like: ``" < > % { } | \ ^ `` and 
reserved characters like: `: / ? # [ ] @ ! $ & ' ( ) * + , ; =` which cannot be used.
As well there is limitation for file upload, the default value for spring.servlet.multipart.max-file-size is 1MB.

#####Why does the size of the modified file increase?

Usaually we use ASCII characters it takes only 7 bits of data to encode them,
 but it can take up to 32 bits (UTF-8) of data to encode a confusable character.

#####How would you increase the density of encoded information?
As an option we can try to encode every n-th character. That way the size of file in total will be smaller
due to not encoded ASCII characters.  

#####How would you protect the encoded information from tampering?
1. Remove encoded message from request parameter to request body
2. Add authorization
3. Add HTTPS

#####What does the throughput of you service depend on?
Throughput of my service depends on hardware like memory usage. 


#####What if you needed to serve 10x clients that the current maximum? 1000x?
Spring Boot uses the `server.tomcat.max-threads` property to control the size of the client request thread pool.
Its default value is zero which leaves Tomcat to use its default of 200. 
To customise the size of this thread pool you should specify value for the server.tomcat.max-threads property
in your application.properties or application.yml file.
Also its depends on the deployment. Using containerization (Docker) and container orchestration (Kubernetes) 
there is no need to increase default max thread pool. As we can just increase number of running containers.
