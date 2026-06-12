Feature: Account Registration

  @regression
  Scenario: Successful Account Registration
    Given the user navigates to Register Account page
    When the user enters the details into below fields
      | firstName | John             |
      | lastName  | Kenedy           |
      | telephone | 1234567890       |
      | password  | bat@12         |
      | email     | bat00@gmail.com |
    And the user selects Privacy Policy
    And the user clicks on Continue button
    Then the user account should get created successfully
