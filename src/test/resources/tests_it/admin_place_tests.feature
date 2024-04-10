Feature: CRUD - Place by Admin

  Scenario: Admin can create a place
    Given I am an admin
    When I create a place
    Then I should see the place in the list of places