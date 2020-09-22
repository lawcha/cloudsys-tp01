window.swaggerSpec={
  "openapi" : "3.0.0",
  "info" : {
    "version" : "1.0.0",
    "title" : "Passion Cuisine API",
    "description" : "API pour passion cuisine"
  },
  "servers" : [ {
    "url" : "http://localhost:8080/"
  }, {
    "url" : "https://virtserver.swaggerhub.com/gdvaud/passion-cuisine/1.0.0"
  }, {
    "url" : "http://virtserver.swaggerhub.com/gdvaud/passion-cuisine/1.0.0"
  } ],
  "tags" : [ {
    "name" : "login",
    "description" : "Operations to login as user or admin"
  }, {
    "name" : "public",
    "description" : "Operations available to everyone"
  }, {
    "name" : "users",
    "description" : "Available to all connected users"
  }, {
    "name" : "admins",
    "description" : "Secured Admin-only calls"
  } ],
  "paths" : {
    "/shoppinglist/{start-day}/{end-day}" : {
      "get" : {
        "summary" : "Gives the shopping list for the current day",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "start-day",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        }, {
          "name" : "end-day",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "The list of shopping for the given days",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "object"
                }
              }
            }
          },
          "400" : {
            "$ref" : "#/components/responses/BadRequest"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/shoppinglist/{start-day}/{end-day}/export" : {
      "get" : {
        "summary" : "Gives the shopping list for the current day",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "start-day",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        }, {
          "name" : "end-day",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "The list in PDF format",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "object"
                }
              }
            }
          },
          "400" : {
            "$ref" : "#/components/responses/BadRequest"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops" : {
      "get" : {
        "summary" : "Get a list of all workshops",
        "tags" : [ "users" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "oneOf" : [ {
                      "$ref" : "#/components/schemas/WorkshopTerminated"
                    }, {
                      "$ref" : "#/components/schemas/WorkshopDraft"
                    } ]
                  }
                }
              }
            }
          }
        }
      },
      "post" : {
        "summary" : "Add a workshop in draft state",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "description" : "The workshop to add",
          "content" : {
            "application/json" : {
              "schema" : {
                "anyOf" : [ {
                  "$ref" : "#/components/schemas/WorkshopDraft"
                }, {
                  "$ref" : "#/components/schemas/WorkshopTerminated"
                } ]
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "400" : {
            "$ref" : "#/components/responses/BadRequest"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops/{title}/{date}" : {
      "get" : {
        "summary" : "Get a workshops",
        "tags" : [ "users" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "title",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "date",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "oneOf" : [ {
                    "$ref" : "#/components/schemas/WorkshopTerminated"
                  }, {
                    "$ref" : "#/components/schemas/WorkshopDraft"
                  } ]
                }
              }
            }
          }
        }
      },
      "put" : {
        "summary" : "Edit a workshop",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "title",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "date",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        } ],
        "requestBody" : {
          "description" : "The workshop new values",
          "content" : {
            "application/json" : {
              "schema" : {
                "anyOf" : [ {
                  "$ref" : "#/components/schemas/WorkshopDraft"
                }, {
                  "$ref" : "#/components/schemas/WorkshopTerminated"
                } ]
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          },
          "404" : {
            "$ref" : "#/components/responses/NotFound"
          }
        }
      },
      "delete" : {
        "summary" : "Delete a published workshop",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "title",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "date",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        } ],
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "400" : {
            "$ref" : "#/components/responses/BadRequest"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops/restricted" : {
      "get" : {
        "summary" : "Get all Workshops for non-connected users",
        "tags" : [ "public" ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/WorkshopRestricted"
                }
              }
            }
          }
        }
      }
    },
    "/workshops/restricted/order/{key}" : {
      "get" : {
        "summary" : "Get all Workshops for non-connected users ordered",
        "tags" : [ "public" ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/WorkshopRestricted"
                }
              }
            }
          }
        }
      }
    },
    "/workshops/export" : {
      "get" : {
        "summary" : "Export workshops list to PDF",
        "deprecated" : true,
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "The PDF file",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "string",
                  "format" : "binary"
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          },
          "404" : {
            "$ref" : "#/components/responses/NotFound"
          }
        }
      }
    },
    "/workshops/{state}" : {
      "get" : {
        "summary" : "Get a list of all workshops filtered by state",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "state",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "enum" : [ "draft", "published", "closed" ]
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "oneOf" : [ {
                      "$ref" : "#/components/schemas/WorkshopTerminated"
                    }, {
                      "$ref" : "#/components/schemas/WorkshopDraft"
                    } ]
                  }
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops/{state}/order/{key}" : {
      "get" : {
        "summary" : "Get a list of all workshops filtered by state",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "state",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "enum" : [ "draft", "published", "closed" ]
          }
        }, {
          "name" : "key",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "oneOf" : [ {
                      "$ref" : "#/components/schemas/WorkshopTerminated"
                    }, {
                      "$ref" : "#/components/schemas/WorkshopDraft"
                    } ]
                  }
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops/{state}/filter/{key}/{value}" : {
      "get" : {
        "summary" : "Get a list of all workshops filtered by state",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "state",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "enum" : [ "draft", "published", "closed" ]
          }
        }, {
          "name" : "key",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "value",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "oneOf" : [ {
                      "$ref" : "#/components/schemas/WorkshopTerminated"
                    }, {
                      "$ref" : "#/components/schemas/WorkshopDraft"
                    } ]
                  }
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops/{title}/{date}/duplicate" : {
      "post" : {
        "summary" : "Duplicate the workshop, given the new id",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "title",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "date",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        } ],
        "requestBody" : {
          "description" : "The workshop id",
          "required" : true,
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/WorkshopRestricted"
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "Duplicated",
            "content" : {
              "application/json" : {
                "schema" : {
                  "anyOf" : [ {
                    "$ref" : "#/components/schemas/WorkshopDraft"
                  }, {
                    "$ref" : "#/components/schemas/WorkshopTerminated"
                  } ]
                }
              }
            }
          },
          "404" : {
            "$ref" : "#/components/responses/NotFound"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops/filter/{key}/{value}" : {
      "get" : {
        "summary" : "Get a list of all workshops filtered",
        "tags" : [ "users" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "key",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "value",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "oneOf" : [ {
                      "$ref" : "#/components/schemas/WorkshopTerminated"
                    }, {
                      "$ref" : "#/components/schemas/WorkshopDraft"
                    } ]
                  }
                }
              }
            }
          }
        }
      }
    },
    "/workshops/order/{key}" : {
      "get" : {
        "summary" : "Get a list of all workshops ordered",
        "tags" : [ "users" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "key",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "oneOf" : [ {
                      "$ref" : "#/components/schemas/WorkshopTerminated"
                    }, {
                      "$ref" : "#/components/schemas/WorkshopDraft"
                    } ]
                  }
                }
              }
            }
          }
        }
      }
    },
    "/workshops/transfer/draft" : {
      "post" : {
        "summary" : "Transfer the given published workshop to a draft",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "description" : "The workshop to transfer",
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/WorkshopDraft"
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "203" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops/transfer/published" : {
      "post" : {
        "summary" : "Transfer the given draft workshop to a published",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "description" : "The workshop to transfer",
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/WorkshopTerminated"
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "203" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/workshops/{title}/{date}/subscriptions" : {
      "get" : {
        "summary" : "Get all subscriptions from the workshop",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "title",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "date",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        }, {
          "name" : "user-email",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "The list of subscription",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "type" : "string"
                  }
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          },
          "404" : {
            "$ref" : "#/components/responses/NotFound"
          }
        }
      }
    },
    "/workshops/{title}/{date}/subscriptions/export" : {
      "get" : {
        "summary" : "Give a pdf file with all the subscriptions",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "title",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "date",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "The PDF file",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "string",
                  "format" : "binary"
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          },
          "404" : {
            "$ref" : "#/components/responses/NotFound"
          }
        }
      }
    },
    "/workshops/{title}/{date}/subscriptions/{user-email}" : {
      "post" : {
        "summary" : "Subscribes the given user to the workshop",
        "tags" : [ "users" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "title",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "date",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        }, {
          "name" : "user-email",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          },
          "404" : {
            "$ref" : "#/components/responses/NotFound"
          }
        }
      },
      "delete" : {
        "summary" : "Unsubscribes the given user from the workshop",
        "tags" : [ "users" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "title",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "date",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string",
            "format" : "date"
          }
        }, {
          "name" : "user-email",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          },
          "404" : {
            "$ref" : "#/components/responses/NotFound"
          }
        }
      }
    },
    "/categories" : {
      "get" : {
        "summary" : "Get the list of all categories",
        "tags" : [ "users" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "$ref" : "#/components/schemas/Category"
                  }
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/locations" : {
      "get" : {
        "summary" : "Get the list of all locations",
        "tags" : [ "public" ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "$ref" : "#/components/schemas/Location"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/organizers" : {
      "get" : {
        "summary" : "Get the list of all organizers",
        "tags" : [ "users" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "OK",
            "content" : {
              "application/json" : {
                "schema" : {
                  "type" : "array",
                  "items" : {
                    "$ref" : "#/components/schemas/Organizer"
                  }
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/organizers/{email}" : {
      "get" : {
        "summary" : "Get the information of a organizer",
        "tags" : [ "admins" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "email",
          "in" : "path",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "The organizer",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/Organizer"
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          },
          "404" : {
            "$ref" : "#/components/responses/NotFound"
          }
        }
      }
    },
    "/users/login" : {
      "post" : {
        "summary" : "Log the user and give back a token for future logins",
        "tags" : [ "login" ],
        "requestBody" : {
          "description" : "The user to log in",
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/User"
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "The user is logged in",
            "headers" : {
              "Authorization" : {
                "description" : "The token to be used for further queries",
                "schema" : {
                  "type" : "string",
                  "format" : "byte"
                }
              }
            }
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    },
    "/users/signup" : {
      "post" : {
        "summary" : "Sign-up the given client",
        "tags" : [ "login" ],
        "requestBody" : {
          "description" : "The client to register",
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/Client"
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "400" : {
            "$ref" : "#/components/responses/BadRequest"
          }
        }
      }
    },
    "/users/logout" : {
      "post" : {
        "summary" : "Log out the given user",
        "tags" : [ "login" ],
        "parameters" : [ {
          "name" : "token",
          "in" : "header",
          "required" : false,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "description" : "The user to disconnect",
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/User"
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "$ref" : "#/components/responses/Success"
          },
          "403" : {
            "$ref" : "#/components/responses/Forbidden"
          }
        }
      }
    }
  },
  "components" : {
    "responses" : {
      "Success" : {
        "description" : "The query were succesful",
        "content" : {
          "application/json" : {
            "schema" : {
              "$ref" : "#/components/schemas/StatusMessage"
            }
          }
        }
      },
      "NotFound" : {
        "description" : "The specified resource was not found",
        "content" : {
          "application/json" : {
            "schema" : {
              "$ref" : "#/components/schemas/StatusMessage"
            }
          }
        }
      },
      "BadRequest" : {
        "description" : "The request is not valid",
        "content" : {
          "application/json" : {
            "schema" : {
              "$ref" : "#/components/schemas/StatusMessage"
            }
          }
        }
      },
      "Forbidden" : {
        "description" : "The specified resource needs a specific right",
        "content" : {
          "application/json" : {
            "schema" : {
              "$ref" : "#/components/schemas/StatusMessage"
            }
          }
        }
      }
    },
    "schemas" : {
      "StatusMessage" : {
        "type" : "object",
        "properties" : {
          "code" : {
            "type" : "string"
          },
          "message" : {
            "type" : "integer"
          }
        },
        "required" : [ "code", "message" ]
      },
      "WorkshopRestricted" : {
        "type" : "object",
        "properties" : {
          "title" : {
            "type" : "string"
          },
          "date" : {
            "type" : "string",
            "format" : "date"
          }
        },
        "required" : [ "title", "date" ]
      },
      "Workshop" : {
        "allOf" : [ {
          "$ref" : "#/components/schemas/WorkshopRestricted"
        } ],
        "type" : "object",
        "properties" : {
          "image" : {
            "type" : "string",
            "format" : "byte"
          },
          "description" : {
            "type" : "string"
          },
          "ingredients" : {
            "type" : "string"
          },
          "minParticipants" : {
            "type" : "integer",
            "format" : "int32",
            "minimum" : 0
          },
          "maxParticipants" : {
            "type" : "integer",
            "format" : "int32",
            "minimum" : 1
          },
          "inscriptionLimit" : {
            "type" : "string",
            "format" : "date-time"
          },
          "emailOrganizer" : {
            "type" : "string"
          },
          "category" : {
            "type" : "string"
          },
          "location" : {
            "$ref" : "#/components/schemas/Location"
          }
        }
      },
      "WorkshopTerminated" : {
        "allOf" : [ {
          "$ref" : "#/components/schemas/Workshop"
        } ],
        "type" : "object",
        "properties" : {
          "publicationDate" : {
            "type" : "string",
            "format" : "date-time"
          },
          "state" : {
            "type" : "string",
            "enum" : [ "published", "closed" ]
          }
        },
        "required" : [ "image", "description", "ingredients", "minParticipants", "maxParticipants", "inscriptionLimit", "emailOrganizer", "category", "publicationDate", "state" ]
      },
      "WorkshopDraft" : {
        "allOf" : [ {
          "$ref" : "#/components/schemas/Workshop"
        } ]
      },
      "Location" : {
        "type" : "object",
        "properties" : {
          "npa" : {
            "type" : "integer",
            "minimum" : 1000,
            "maximum" : 10000,
            "exclusiveMaximum" : true
          },
          "city" : {
            "type" : "string"
          }
        }
      },
      "Category" : {
        "type" : "object",
        "properties" : {
          "title" : {
            "type" : "string"
          },
          "description" : {
            "type" : "string"
          }
        }
      },
      "Organizer" : {
        "type" : "object",
        "properties" : {
          "email" : {
            "type" : "string"
          },
          "firstName" : {
            "type" : "string"
          },
          "lastName" : {
            "type" : "string"
          },
          "phone" : {
            "type" : "string"
          },
          "street" : {
            "type" : "string"
          },
          "location" : {
            "$ref" : "#/components/schemas/Location"
          }
        }
      },
      "User" : {
        "type" : "object",
        "properties" : {
          "email" : {
            "type" : "string"
          },
          "password" : {
            "type" : "string",
            "format" : "password"
          }
        },
        "required" : [ "email" ]
      },
      "Client" : {
        "allOf" : [ {
          "$ref" : "#/components/schemas/User"
        } ],
        "type" : "object",
        "properties" : {
          "firstName" : {
            "type" : "string"
          },
          "lastName" : {
            "type" : "string"
          },
          "phone" : {
            "type" : "string"
          },
          "street" : {
            "type" : "string"
          },
          "location" : {
            "$ref" : "#/components/schemas/Location"
          }
        },
        "required" : [ "firstName", "lastName", "street", "location" ]
      }
    }
  }
}