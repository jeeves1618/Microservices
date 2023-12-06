Notes

If you are creating table, write the DDLs for creating the table in schema.sql under resources folder. This SQLs will get executed on startup. If the table is already created, this step will be ignored.

If your table column name abides by the SQL standard of column_name and the field names within @Entity class is defined in camel case columnName, then you don't need the @Column annotation.

When the same set of columns are repeated across tables in a project, those columns are bundled in to a class with the annotation @MappedSuperclass. Then this base class can be extended in the @Entity classes. 

@Column(updatable = false) will ensure that the column is not updated during the updates.
@Column(insertable = false) will ensure that the column is not populated when a record is inserted the first time.

spring-boot-starter-validation is the dependency for data validation of user inputs.

Unlike reads, update or delete using custom methods requires two annotations: @Modifying - to tell Spring JPA that this method is going update and run this query within a transaction and @Transactional - to ensure that the unit of work is completed in its entiriety and two phase commit is performed.