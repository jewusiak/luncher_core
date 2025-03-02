Feature: Menu-offers management in places

  Background:
    Given User exists:
      | uuid                                 | email                  | password | name     | surname | role         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e3 | user@luncher.corp      | 1234     | Zbigniew | Json    | USER         |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e6 | rmanager@luncher.corp  | 1234     | MAN      | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e4 | rmanager2@luncher.corp | 1234     | MAN2     | Json2   | REST_MANAGER |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e5 | admin@luncher.corp     | 1234     | ADMIN    | Json2   | SYS_ADMIN    |
      | 3e594d22-ae9f-4ded-8c1b-6fcb306b40e7 | mod@luncher.corp       | 1234     | MOD      | Json2   | SYS_MOD      |

    And place types exist:
      | identifier | iconName   | name                |
      | RESTAURANT | restaurant | Restaurant category |
      | BAR        | bar        | Bar category name   |
    
    # Monday
    And Simulated time is 2025-01-06T00:00:00


  Scenario: Add, update, delete place menu offer
    And User logs in using credentials:
      | email                 | password |
      | rmanager@luncher.corp | 1234     |
    And response code is 200
    And User is logged in as rmanager@luncher.corp

    And User creates a place as below ID 1:
      | name            | description                                      | placeTypeIdentifier | location.latitude | location.longitude | enabled |
      | The Cool Cat TR | Restauracja typu asian fusion w centrum Warszawy | RESTAURANT          | 52.21507395584024 | 21.02108986309555  | true    |
    And response code is 200

    When Send GET request to /place/[ID:PLACE:1] without body
    Then response code is 200
    And HTTP Response has a list of size 0 in path menuOffers

    When Send PUT request to /place/[ID:PLACE:1] with body as below:
    """
{
  "menuOffers": [
    {
      "name": "Lunch Monday Special",
      "basePrice": {
        "amount": 39.99,
        "currencyCode": "PLN"
      },
      "parts": [
        {
          "name": "First Course",
          "required": true,
          "supplement": {
            "amount": 0,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Tomato Soup",
              "description": "Classic tomato soup with basil and cream",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Tripe Soup",
              "description": "Traditional Polish tripe soup",
              "supplement": {
                "amount": 2.5,
                "currencyCode": "PLN"
              }
            }
          ]
        },
        {
          "name": "Main Course",
          "required": true,
          "supplement": {
            "amount": 0,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Grilled Chicken Breast",
              "description": "Served with mashed potatoes and steamed vegetables",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Pork Schnitzel",
              "description": "Breaded pork cutlet served with fries and salad",
              "supplement": {
                "amount": 3,
                "currencyCode": "PLN"
              }
            }
          ]
        },
        {
          "name": "Dessert",
          "required": false,
          "supplement": {
            "amount": 5,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Apple Pie",
              "description": "Warm apple pie with vanilla ice cream",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Cheesecake",
              "description": "Creamy cheesecake with raspberry sauce",
              "supplement": {
                "amount": 1.5,
                "currencyCode": "PLN"
              }
            }
          ]
        }
      ],
      "recurringServingRanges": [
        {
          "startTime": {
            "time": "11:00",
            "day": "MONDAY"
          },
          "endTime": {
            "time": "14:00",
            "day": "MONDAY"
          }
        }
      ],
      "oneTimeServingRanges": []
    }
  ]
}
    """
    Then response code is 200
    And Send GET request to /place/[ID:PLACE:1] without body
    And response code is 200
    And HTTP Response is:
      | menuOffers[0].name   | menuOffers[0].basePrice.amount | menuOffers[0].basePrice.currencyCode | menuOffers[0].parts[0].name | menuOffers[0].parts[0].required | menuOffers[0].parts[0].supplement.amount | menuOffers[0].parts[0].supplement.currencyCode | menuOffers[0].parts[0].options[0].name | menuOffers[0].parts[0].options[0].description | menuOffers[0].parts[0].options[0].supplement.amount | menuOffers[0].parts[0].options[0].supplement.currencyCode | menuOffers[0].parts[0].options[1].name | menuOffers[0].parts[0].options[1].description | menuOffers[0].parts[0].options[1].supplement.amount | menuOffers[0].parts[0].options[1].supplement.currencyCode | menuOffers[0].parts[1].name | menuOffers[0].parts[1].required | menuOffers[0].parts[1].supplement.amount | menuOffers[0].parts[1].supplement.currencyCode | menuOffers[0].parts[1].options[0].name | menuOffers[0].parts[1].options[0].description      | menuOffers[0].parts[1].options[0].supplement.amount | menuOffers[0].parts[1].options[0].supplement.currencyCode | menuOffers[0].parts[1].options[1].name | menuOffers[0].parts[1].options[1].description   | menuOffers[0].parts[1].options[1].supplement.amount | menuOffers[0].parts[1].options[1].supplement.currencyCode | menuOffers[0].parts[2].name | menuOffers[0].parts[2].required | menuOffers[0].parts[2].supplement.amount | menuOffers[0].parts[2].supplement.currencyCode | menuOffers[0].parts[2].options[0].name | menuOffers[0].parts[2].options[0].description | menuOffers[0].parts[2].options[0].supplement.amount | menuOffers[0].parts[2].options[0].supplement.currencyCode | menuOffers[0].parts[2].options[1].name | menuOffers[0].parts[2].options[1].description | menuOffers[0].parts[2].options[1].supplement.amount | menuOffers[0].parts[2].options[1].supplement.currencyCode | menuOffers[0].recurringServingRanges[0].startTime.time | menuOffers[0].recurringServingRanges[0].startTime.day | menuOffers[0].recurringServingRanges[0].endTime.time | menuOffers[0].recurringServingRanges[0].endTime.day | menuOffers[0].thisOrNextServingRange.startTime | menuOffers[0].thisOrNextServingRange.endTime |
      | Lunch Monday Special | 39.99                          | PLN                                  | First Course                | true                            | 0.0                                      | PLN                                            | Tomato Soup                            | Classic tomato soup with basil and cream      | 0.0                                                 | PLN                                                       | Tripe Soup                             | Traditional Polish tripe soup                 | 2.5                                                 | PLN                                                       | Main Course                 | true                            | 0.0                                      | PLN                                            | Grilled Chicken Breast                 | Served with mashed potatoes and steamed vegetables | 0.0                                                 | PLN                                                       | Pork Schnitzel                         | Breaded pork cutlet served with fries and salad | 3.0                                                 | PLN                                                       | Dessert                     | false                           | 5.0                                      | PLN                                            | Apple Pie                              | Warm apple pie with vanilla ice cream         | 0.0                                                 | PLN                                                       | Cheesecake                             | Creamy cheesecake with raspberry sauce        | 1.5                                                 | PLN                                                       | 11:00                                                  | MONDAY                                                | 14:00                                                | MONDAY                                              | 2025-01-06T11:00:00                            | 2025-01-06T14:00:00                          |


    When Send PUT request to /place/[ID:PLACE:1] with body as below:
    """
   {
  "menuOffers": [
    {
      "name": "Lunch Monday Special",
      "basePrice": {
        "amount": 39.99,
        "currencyCode": "PLN"
      },
      "parts": [
        {
          "name": "First Course",
          "required": true,
          "supplement": {
            "amount": 0,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Tomato Soup",
              "description": "Classic tomato soup with basil and cream",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Tripe Soup",
              "description": "Traditional Polish tripe soup",
              "supplement": {
                "amount": 2.5,
                "currencyCode": "PLN"
              }
            }
          ]
        },
        {
          "name": "Main Course",
          "required": true,
          "supplement": {
            "amount": 0,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Grilled Chicken Breast",
              "description": "Served with mashed potatoes and steamed vegetables",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Pork Schnitzel",
              "description": "Breaded pork cutlet served with fries and salad",
              "supplement": {
                "amount": 3,
                "currencyCode": "PLN"
              }
            }
          ]
        },
        {
          "name": "Dessert",
          "required": false,
          "supplement": {
            "amount": 5,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Apple Pie",
              "description": "Warm apple pie with vanilla ice cream",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Cheesecake",
              "description": "Creamy cheesecake with raspberry sauce",
              "supplement": {
                "amount": 1.5,
                "currencyCode": "PLN"
              }
            }
          ]
        }
      ],
      "recurringServingRanges": [
        {
          "startTime": {
            "time": "11:00",
            "day": "MONDAY"
          },
          "endTime": {
            "time": "14:00",
            "day": "MONDAY"
          }
        }
      ],
      "oneTimeServingRanges": []
    },
    {
      "name": "Lunch Tuesday Special",
      "basePrice": {
        "amount": 45.50,
        "currencyCode": "PLN"
      },
      "parts": [
        {
          "name": "Appetizer",
          "required": true,
          "supplement": {
            "amount": 0.00,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Bruschetta",
              "description": "Grilled bread topped with tomatoes, garlic, and basil",
              "supplement": {
                "amount": 0.00,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Garlic Bread",
              "description": "Crispy bread with garlic butter and herbs",
              "supplement": {
                "amount": 2.00,
                "currencyCode": "PLN"
              }
            }
          ]
        },
        {
          "name": "Main Course",
          "required": true,
          "supplement": {
            "amount": 0.00,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Beef Steak",
              "description": "Grilled beef steak served with roasted potatoes and salad",
              "supplement": {
                "amount": 5.00,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Vegetarian Lasagna",
              "description": "Lasagna with spinach, ricotta, and tomato sauce",
              "supplement": {
                "amount": 0.00,
                "currencyCode": "PLN"
              }
            }
          ]
        },
        {
          "name": "Dessert",
          "required": false,
          "supplement": {
            "amount": 7.00,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Tiramisu",
              "description": "Classic Italian dessert with coffee and mascarpone",
              "supplement": {
                "amount": 0.00,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Chocolate Mousse",
              "description": "Rich chocolate mousse with whipped cream",
              "supplement": {
                "amount": 1.50,
                "currencyCode": "PLN"
              }
            }
          ]
        }
      ],
      "recurringServingRanges": [
        {
          "startTime": {
            "time": "11:00",
            "day": "TUESDAY"
          },
          "endTime": {
            "time": "14:00",
            "day": "TUESDAY"
          }
        }
      ],
      "oneTimeServingRanges": [
        {
          "startTime": "2024-10-20T10:00:00",
          "endTime": "2024-10-20T20:34:00"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And Send GET request to /place/[ID:PLACE:1] without body
    And response code is 200
    And HTTP Response has a list of size 2 in path menuOffers
    And HTTP Response is:
      | menuOffers[1].name    | menuOffers[1].basePrice.amount | menuOffers[1].basePrice.currencyCode | menuOffers[1].parts[0].name | menuOffers[1].parts[0].required | menuOffers[1].parts[0].supplement.amount | menuOffers[1].parts[0].supplement.currencyCode | menuOffers[1].parts[0].options[0].name | menuOffers[1].parts[0].options[0].description         | menuOffers[1].parts[0].options[0].supplement.amount | menuOffers[1].parts[0].options[0].supplement.currencyCode | menuOffers[1].parts[0].options[1].name | menuOffers[1].parts[0].options[1].description | menuOffers[1].parts[0].options[1].supplement.amount | menuOffers[1].parts[0].options[1].supplement.currencyCode | menuOffers[1].parts[1].name | menuOffers[1].parts[1].required | menuOffers[1].parts[1].supplement.amount | menuOffers[1].parts[1].supplement.currencyCode | menuOffers[1].parts[1].options[0].name | menuOffers[1].parts[1].options[0].description             | menuOffers[1].parts[1].options[0].supplement.amount | menuOffers[1].parts[1].options[0].supplement.currencyCode | menuOffers[1].parts[1].options[1].name | menuOffers[1].parts[1].options[1].description   | menuOffers[1].parts[1].options[1].supplement.amount | menuOffers[1].parts[1].options[1].supplement.currencyCode | menuOffers[1].parts[2].name | menuOffers[1].parts[2].required | menuOffers[1].parts[2].supplement.amount | menuOffers[1].parts[2].supplement.currencyCode | menuOffers[1].parts[2].options[0].name | menuOffers[1].parts[2].options[0].description      | menuOffers[1].parts[2].options[0].supplement.amount | menuOffers[1].parts[2].options[0].supplement.currencyCode | menuOffers[1].parts[2].options[1].name | menuOffers[1].parts[2].options[1].description | menuOffers[1].parts[2].options[1].supplement.amount | menuOffers[1].parts[2].options[1].supplement.currencyCode | menuOffers[1].recurringServingRanges[0].startTime.time | menuOffers[1].recurringServingRanges[0].startTime.day | menuOffers[1].recurringServingRanges[0].endTime.time | menuOffers[1].recurringServingRanges[0].endTime.day | menuOffers[1].oneTimeServingRanges[0].startTime | menuOffers[1].oneTimeServingRanges[0].endTime | menuOffers[1].thisOrNextServingRange.startTime | menuOffers[1].thisOrNextServingRange.endTime | menuOffers[0].name   | menuOffers[0].basePrice.amount | menuOffers[0].basePrice.currencyCode | menuOffers[0].parts[0].name | menuOffers[0].parts[0].required | menuOffers[0].parts[0].supplement.amount | menuOffers[0].parts[0].supplement.currencyCode | menuOffers[0].parts[0].options[0].name | menuOffers[0].parts[0].options[0].description | menuOffers[0].parts[0].options[0].supplement.amount | menuOffers[0].parts[0].options[0].supplement.currencyCode | menuOffers[0].parts[0].options[1].name | menuOffers[0].parts[0].options[1].description | menuOffers[0].parts[0].options[1].supplement.amount | menuOffers[0].parts[0].options[1].supplement.currencyCode | menuOffers[0].parts[1].name | menuOffers[0].parts[1].required | menuOffers[0].parts[1].supplement.amount | menuOffers[0].parts[1].supplement.currencyCode | menuOffers[0].parts[1].options[0].name | menuOffers[0].parts[1].options[0].description      | menuOffers[0].parts[1].options[0].supplement.amount | menuOffers[0].parts[1].options[0].supplement.currencyCode | menuOffers[0].parts[1].options[1].name | menuOffers[0].parts[1].options[1].description   | menuOffers[0].parts[1].options[1].supplement.amount | menuOffers[0].parts[1].options[1].supplement.currencyCode | menuOffers[0].parts[2].name | menuOffers[0].parts[2].required | menuOffers[0].parts[2].supplement.amount | menuOffers[0].parts[2].supplement.currencyCode | menuOffers[0].parts[2].options[0].name | menuOffers[0].parts[2].options[0].description | menuOffers[0].parts[2].options[0].supplement.amount | menuOffers[0].parts[2].options[0].supplement.currencyCode | menuOffers[0].parts[2].options[1].name | menuOffers[0].parts[2].options[1].description | menuOffers[0].parts[2].options[1].supplement.amount | menuOffers[0].parts[2].options[1].supplement.currencyCode | menuOffers[0].recurringServingRanges[0].startTime.time | menuOffers[0].recurringServingRanges[0].startTime.day | menuOffers[0].recurringServingRanges[0].endTime.time | menuOffers[0].recurringServingRanges[0].endTime.day | menuOffers[0].thisOrNextServingRange.startTime | menuOffers[0].thisOrNextServingRange.endTime |
      | Lunch Tuesday Special | 45.5                           | PLN                                  | Appetizer                   | true                            | 0.0                                      | PLN                                            | Bruschetta                             | Grilled bread topped with tomatoes, garlic, and basil | 0.0                                                 | PLN                                                       | Garlic Bread                           | Crispy bread with garlic butter and herbs     | 2.0                                                 | PLN                                                       | Main Course                 | true                            | 0.0                                      | PLN                                            | Beef Steak                             | Grilled beef steak served with roasted potatoes and salad | 5.0                                                 | PLN                                                       | Vegetarian Lasagna                     | Lasagna with spinach, ricotta, and tomato sauce | 0.0                                                 | PLN                                                       | Dessert                     | false                           | 7.0                                      | PLN                                            | Tiramisu                               | Classic Italian dessert with coffee and mascarpone | 0.0                                                 | PLN                                                       | Chocolate Mousse                       | Rich chocolate mousse with whipped cream      | 1.5                                                 | PLN                                                       | 11:00                                                  | TUESDAY                                               | 14:00                                                | TUESDAY                                             | 2024-10-20T10:00:00                             | 2024-10-20T20:34:00                           | 2025-01-07T11:00:00                            | 2025-01-07T14:00:00                          | Lunch Monday Special | 39.99                          | PLN                                  | First Course                | true                            | 0.0                                      | PLN                                            | Tomato Soup                            | Classic tomato soup with basil and cream      | 0.0                                                 | PLN                                                       | Tripe Soup                             | Traditional Polish tripe soup                 | 2.5                                                 | PLN                                                       | Main Course                 | true                            | 0.0                                      | PLN                                            | Grilled Chicken Breast                 | Served with mashed potatoes and steamed vegetables | 0.0                                                 | PLN                                                       | Pork Schnitzel                         | Breaded pork cutlet served with fries and salad | 3.0                                                 | PLN                                                       | Dessert                     | false                           | 5.0                                      | PLN                                            | Apple Pie                              | Warm apple pie with vanilla ice cream         | 0.0                                                 | PLN                                                       | Cheesecake                             | Creamy cheesecake with raspberry sauce        | 1.5                                                 | PLN                                                       | 11:00                                                  | MONDAY                                                | 14:00                                                | MONDAY                                              | 2025-01-06T11:00:00                            | 2025-01-06T14:00:00                          |

    And Put ID MENU_OFFER idx 0 to cache from HTTP response from path menuOffers[0].id
    And Put ID MENU_OFFER idx 1 to cache from HTTP response from path menuOffers[1].id

#    DELETE
    When Send PUT request to /place/[ID:PLACE:1] with body as below:
    """
{
  "menuOffers": [
    {
      "name": "Lunch Monday Special",
      "basePrice": {
        "amount": 39.99,
        "currencyCode": "PLN"
      },
      "parts": [
        {
          "name": "First Course",
          "required": true,
          "supplement": {
            "amount": 0,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Tomato Soup",
              "description": "Classic tomato soup with basil and cream",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Tripe Soup",
              "description": "Traditional Polish tripe soup",
              "supplement": {
                "amount": 2.5,
                "currencyCode": "PLN"
              }
            }
          ]
        },
        {
          "name": "Main Course",
          "required": true,
          "supplement": {
            "amount": 0,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Grilled Chicken Breast",
              "description": "Served with mashed potatoes and steamed vegetables",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Pork Schnitzel",
              "description": "Breaded pork cutlet served with fries and salad",
              "supplement": {
                "amount": 3,
                "currencyCode": "PLN"
              }
            }
          ]
        },
        {
          "name": "Dessert",
          "required": false,
          "supplement": {
            "amount": 5,
            "currencyCode": "PLN"
          },
          "options": [
            {
              "name": "Apple Pie",
              "description": "Warm apple pie with vanilla ice cream",
              "supplement": {
                "amount": 0,
                "currencyCode": "PLN"
              }
            },
            {
              "name": "Cheesecake",
              "description": "Creamy cheesecake with raspberry sauce",
              "supplement": {
                "amount": 1.5,
                "currencyCode": "PLN"
              }
            }
          ]
        }
      ],
      "recurringServingRanges": [
        {
          "startTime": {
            "time": "11:00",
            "day": "MONDAY"
          },
          "endTime": {
            "time": "14:00",
            "day": "MONDAY"
          }
        }
      ],
      "oneTimeServingRanges": [
        {
          "startTime": "2025-01-06T09:00:00",
          "endTime": "2025-01-06T09:30:00"
        }
      ]
    }
  ]
}
    """
    Then response code is 200
    And Send GET request to /place/[ID:PLACE:1] without body
    And response code is 200
    And HTTP Response is:
      | menuOffers[0].name   | menuOffers[0].basePrice.amount | menuOffers[0].basePrice.currencyCode | menuOffers[0].parts[0].name | menuOffers[0].parts[0].required | menuOffers[0].parts[0].supplement.amount | menuOffers[0].parts[0].supplement.currencyCode | menuOffers[0].parts[0].options[0].name | menuOffers[0].parts[0].options[0].description | menuOffers[0].parts[0].options[0].supplement.amount | menuOffers[0].parts[0].options[0].supplement.currencyCode | menuOffers[0].parts[0].options[1].name | menuOffers[0].parts[0].options[1].description | menuOffers[0].parts[0].options[1].supplement.amount | menuOffers[0].parts[0].options[1].supplement.currencyCode | menuOffers[0].parts[1].name | menuOffers[0].parts[1].required | menuOffers[0].parts[1].supplement.amount | menuOffers[0].parts[1].supplement.currencyCode | menuOffers[0].parts[1].options[0].name | menuOffers[0].parts[1].options[0].description      | menuOffers[0].parts[1].options[0].supplement.amount | menuOffers[0].parts[1].options[0].supplement.currencyCode | menuOffers[0].parts[1].options[1].name | menuOffers[0].parts[1].options[1].description   | menuOffers[0].parts[1].options[1].supplement.amount | menuOffers[0].parts[1].options[1].supplement.currencyCode | menuOffers[0].parts[2].name | menuOffers[0].parts[2].required | menuOffers[0].parts[2].supplement.amount | menuOffers[0].parts[2].supplement.currencyCode | menuOffers[0].parts[2].options[0].name | menuOffers[0].parts[2].options[0].description | menuOffers[0].parts[2].options[0].supplement.amount | menuOffers[0].parts[2].options[0].supplement.currencyCode | menuOffers[0].parts[2].options[1].name | menuOffers[0].parts[2].options[1].description | menuOffers[0].parts[2].options[1].supplement.amount | menuOffers[0].parts[2].options[1].supplement.currencyCode | menuOffers[0].recurringServingRanges[0].startTime.time | menuOffers[0].recurringServingRanges[0].startTime.day | menuOffers[0].recurringServingRanges[0].endTime.time | menuOffers[0].recurringServingRanges[0].endTime.day | menuOffers[0].thisOrNextServingRange.startTime | menuOffers[0].thisOrNextServingRange.endTime |
      | Lunch Monday Special | 39.99                          | PLN                                  | First Course                | true                            | 0.0                                      | PLN                                            | Tomato Soup                            | Classic tomato soup with basil and cream      | 0.0                                                 | PLN                                                       | Tripe Soup                             | Traditional Polish tripe soup                 | 2.5                                                 | PLN                                                       | Main Course                 | true                            | 0.0                                      | PLN                                            | Grilled Chicken Breast                 | Served with mashed potatoes and steamed vegetables | 0.0                                                 | PLN                                                       | Pork Schnitzel                         | Breaded pork cutlet served with fries and salad | 3.0                                                 | PLN                                                       | Dessert                     | false                           | 5.0                                      | PLN                                            | Apple Pie                              | Warm apple pie with vanilla ice cream         | 0.0                                                 | PLN                                                       | Cheesecake                             | Creamy cheesecake with raspberry sauce        | 1.5                                                 | PLN                                                       | 11:00                                                  | MONDAY                                                | 14:00                                                | MONDAY                                              | 2025-01-06T09:00:00                            | 2025-01-06T09:30:00                          |
