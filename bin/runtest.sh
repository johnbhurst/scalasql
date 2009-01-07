#!/bin/bash

scala -cp out/production/scalasql/:out/test/scalasql/:lib/ojdbc14-10.2.0.3.0.jar \
  nz.co.skepticalhumorist.sql.TestApp

