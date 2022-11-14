# e-commerce-platform-API-practice
Kotlin GraphQL Spring Boot

This is the first release, 
there are still a couple of things to fix and some improvements for sure, I have attached a copy of the database, 
this could also be deploy with pipelines to a container, but that's a pending implementation.

The database access, GraphQL and GraphiQL can abe configured in the file application.yml
as well, the following queries can be used to access the information.

GraphQL sapmple queries:
<h3>All Sales</h3>
<pre>
query {
	sales{
		id
		sales
		payment_method
		datetime
	}
}
</pre>
* Important to mentionthat for sales I'm allowing only to access the fields: id, sales, payment_method, datetime; 
This could be extended to all the fields in the configuration.

<h3>Interval Filter</h3>
sales were made within a date range broken down into hours.
<pre>
query {
	salesInterval(filter: {
			startDateTime: "2022-11-14T21:55:08.000+09:00"
			endDateTime: "2022-11-14T23:04:08.000+09:00"
		}) {
		id
		sales
		payment_method
		datetime
		
	}
}
</pre>
* It's important to mention that if the startDateTime or endDateTime are not provided, the missing date will be set to now.

<h3>Create Payment</h3>
<pre>
mutation {
	createPayment(
		customer_id: "12345"
		price: "18.00"
		price_modifier: 1.95
		payment_method: "VISA"
		datetime: "2022-09-01T00:00:00Z"
		additional_item: {
			last_4: "5432"
		}
	){
		final_price
		points
	}
}
</pre>
* When creating the payment the validation that can break the request is the related to the additional information, 
when provided unecessary information the response will be indicating the error that was found, as well the request 
will be interrupted since it was a bad request. In case of the values to calculate the points and the final price,
the request is processed even when they input a very high number for calculation, the values are truncated on the 
max and min allowed values.

<h3>Pending Issues</h3>
I didn't had enought time to dedicate this task, there are pending issues:
<li>Dates format validation</li>
<li>Input data validations</li>
<li>Test cases</li>
<li>Improve style for the error messages</li>

<h3>Scalability</h3>
For improving the performance of this application there are a few things to consider.
<h4>Application Traffic</h4>
We need to think on the tarjet audience for the next couple of years and evaluate the impact and identify base on 
the traffic what are the current needs. As an example we could think on this application and identify that it could be
that at the end of the month our site will be having a heavy traffic due to the pay day for many people to be at the end
of the month, as well for the companies to spend the budged of the month, having this in mind and with some historics,
we could identify that we need to increase our resources for those heavy days with a cloud solution.

<h4>Cost</h4>
We can always increase the resources, but at a cost, we need to think about having the resources at the right time, if we were
to have the top tier of the cloud plans thinking in having enough to provide the service it would cost a tons of money for the 
company, is becaus of this that we need to keep a good up to date inventory, ensuring that we are only paying for what we really need.

<h4>Planning</h4>
So the solution to all this is the planning, we need to think ahead and estimate base on the historics about what we will be requiring.

<h4>Implementation</h4>
Now being said all this and getting back to the project, I would recommend to implement this in as follows:
<li><b>CI/CD.</b> This would help our team to deploy faster, secure and with quality. Also this would integrate well with containers.</li>
<li><b>Containers.</b> This will allow the project to be deployed on demand, if there is a need of more resources, then we can just add 
more containers with the same deployment.</li>
<li><b>Database Cluster.</b> This will ensure highly availability and to improve the performance.</li>
<li><b>RDS.</b> If there is high demand and multiple locations, this would boost performance with a price.</li>
<li><b>API Gateway.</b> This would help persformance doing cache of the requests and reducing the access to the database for information 
that is constantly requested.</li>
