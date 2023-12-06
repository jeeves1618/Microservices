Notes

@RestController will expose all methods. If you want expose select methods, you can use @Controller on top of the class and @ResponseBody on top of the methods.

If you are creating table, write the DDLs for creating the table in schema.sql under resources folder. This SQLs will get executed on startup. If the table is already created, this step will be ignored.

If your table column name abides by the SQL standard of column_name and the field names within @Entity class is defined in camel case columnName, then you don't need the @Column annotation.

When the same set of columns are repeated across tables in a project, those columns are bundled in to a class with the annotation @MappedSuperclass. Then this base class can be extended in the @Entity classes. 

@Column(updatable = false) will ensure that the column is not updated during the updates.
@Column(insertable = false) will ensure that the column is not populated when a record is inserted the first time.

Unlike reads, update or delete using custom methods requires two annotations: @Modifying - to tell Spring JPA that this method is going update and run this query within a transaction and @Transactional - to ensure that the unit of work is completed in its entiriety and two phase commit is performed.

Validation: 

spring-boot-starter-validation is the dependency for data validation of user inputs. These are the most common spring validations.

@NotNull: Ensures a field is not null.
@NotBlank: Enforces non-nullity and requires at least one non-whitespace character.
@NotEmpty: Guarantees that collections or arrays are not empty.
@Min(value): Checks if a numeric field is greater than or equal to the specified minimum value.
@Max(value): Checks if a numeric field is less than or equal to the specified maximum value.
@Size(min, max): Validates if a string or collection size is within a specific range.
@Pattern(regex): Verifies if a field matches the provided regular expression.
@Email: Ensures a field contains a valid email address format.
@Digits(integer, fraction): Validates that a numeric field has a specified number of integer and fraction digits.
@Past and @Future : Checks that a date or time field is in the past and future respectively.
@AssertTrue and @AssertFalse: Ensures that a boolean field is true. and false respectively.
@CreditCardNumber: Validates that a field contains a valid credit card number.
@Valid: Triggers validation of nested objects or properties.
@Validated: Specifies validation groups to be applied at the class or method level.

@Validated along with @RestController is a class-level annotation that we can use to tell Spring to validate parameters that are passed into a method of the annotated class.

For spring to do the validations in a data object, we have to place @Valid before the object being passed as parameter. @Valid will work only before classes. It will not work before fields. Before the fields, we will have to do the actual validation. 

Just adding the validation like @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits") before the fields (either in classes or in method parameters) is only half way through. We should also tell the spring how to through the exception. For that we have to write a method that returns ResponseEntity<Object> and takes in MethodArgumentNotValidException, HttpHeaders, HttpStatusCode, WebRequest as parameters. This method is part of ResponseEntityExceptionHandler class and it should be extended in our global exception class. This will streamline the exception messages for client's consumption

Audit:

Use the AuditorAware interface to capture the audit information. Implement getCurrentAuditor method within AuditorAware. Give it a component name. Enable the @EnableJpaAuditing in the spring boot application with auditorAwareRef pointing to the AuditAware implementation's component name. Add @EntityListeners(AuditingEntityListener.class) to the entity (or base entity) whose columns should be updated.

Documentation of REST APIs (using https://springdoc.org/):

Just adding the dependency will take care of swagger documentation under http://localhost:8080/swagger-ui/index.html. But the documentation will be a basic one. Use @OpenAPIDefinition annotation in the main class to customize the documentation. 

The controller name can be overriden by @Tag

Also, the basic doc will show the api path. if we want to document the operations, we can use @Operation annotation. 

Similarly, the response in swagger will show only the default 200 status. We can add more using @ApiResponse

If you want to give example values in the documentation, use @Example