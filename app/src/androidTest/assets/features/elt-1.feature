Feature: Display a list of items

  Create an android app to display the tasks as shown in 'design.png'

  # Due to time constraints I was not able to complete this list.
  # However I would have included the following tests:

  # Scenario: Tasks should load when offline
  # Scenario: Download fails
  # Scenario: Filtering
  # Scenario: Types to icons
  # Scenario: Reload

  Scenario: App loads list
    When the list screen is started
    Then I will see that the app is loading data

  Scenario: No tasks
    Given no tasks stored locally
    When the list screen is started
    Then I will see a message telling me there are no tasks

  Scenario Outline: Task shows correctly
    Given no tasks stored locally
    And the list screen is started
    When the server returns a task with name "<name>", description "<description>", and type "<type>"
    Then I will see the name "<name>"
    And I will see the description "<description>"

    Examples:
    | name | description | type |
    | Take the rubbish out | Empty the bin | general |
    | Make a hot drink | Make David a cup of tea | hydration |
    | 5 ml Azopt 10mg/1ml | Instil one drop to both eyes at the morning | medication |

  Scenario: Offline Banner
    Given the app is offline
    When the list screen is started
    Then I will see an offline banner telling me the app is offline
