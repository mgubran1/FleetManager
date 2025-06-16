## Database Schema Updater

If you have updated your Employee model or Java code and encounter errors about missing columns in the SQLite database, use the schema updater utility before running the main application.

### How to Run

1. Compile the updater:
    ```sh
    javac -cp . src/main/java/com/semitruck/DbSchemaUpdater.java
    ```

2. Run the updater:
    ```sh
    java -cp . com.semitruck.DbSchemaUpdater
    ```

- This will automatically add any missing columns to the `employees` table in `fleetapp.db`.
- You can safely run this utility multiple times; it will not duplicate columns.
- Run this utility after every code update that adds/removes Employee fields.

**Tip:** You can also call this utility from your main app at startup for automatic migrations!