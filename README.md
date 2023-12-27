# Quantumsurvey

# Overview
# Quantumsurvey

# Overview

Welcome to the Quantumsurvey repository! This README provides essential information for developers working on this project. Please follow the guidelines outlined below to ensure a smooth development process.

## Table of contents

<!-- * [Live](#live) -->
* [Branching Strategy](#Branch)
* [Development Workflow](#Developer)
* [Code Review Guidelines](#Review)

  
## <a name="Branch"></a>Branching Strategy
<p>We follow a Git branching strategy to manage different aspects of development. Here are the branches:

#### master:
The master branch represents production-ready code. It should always reflect the latest release.

#### dev: 
The dev branch is where features are integrated and tested. Developers should create feature branches from this branch.

#### Feature Branches: 
For every new feature or bug fix, create a feature branch off of develop. Use a descriptive branch name (e.g., feature/new-feature or bugfix/fix-issue).</p>
<pre>
# Example workflow for creating a new feature branch
git checkout dev
git pull origin dev
git checkout -b dev/your_name
</pre>

## <a name="Developer"></a>Development Workflow
**1. Clone the Repository:** Clone the repository to your local machine using the following command:
<pre>
git clone --branch dev https://github.com/qntmnet/Quantumsurvey.git
</pre>

**2. Create a Branch:** Before making any changes, create a new branch for your feature or bug fix. 
<pre>
git checkout -b dev/your_name
</pre>

**3. Make Changes:** Implement the necessary changes in your branch. 

**4. Commit Changes:** Commit your changes with a clear and concise message.
<pre>
git add .
git commit -m "Implement new feature: XYZ"
</pre>

**5. Push Changes:** Push your changes to the remote repository.
<pre>
git push -u origin dev/your_name
</pre>

**Notes :** Follow below steps on GitHub UI.  
**1. Create a Pull Request (PR):** When your feature is ready, create a PR against the dev branch.  
**2. Code Review:** Your code will be reviewed by other team members. Make necessary adjustments if required.  
**3. Merge to Develop:** Once the PR is approved, merge your changes into the develop branch.  
**4. Release Preparation:**  When a set of features is ready for release, create a PR from develop to main and follow the release process.  
#### Note : -
After completion of above mentioned steps, in case you want to continue your work then you are requested to continue your work in dev branch in your local system. 
<pre>
git checkout dev
</pre>
## <a name="Review"></a>Code Review Guidelines

* Ensure your code follows the project's coding standards.   
* Write clear and concise code.  
* Include unit tests for new features or bug fixes.  
* Respond promptly to feedback during code reviews.  
  




Welcome to the Quantumsurvey repository! This README provides essential information for developers working on this project. Please follow the guidelines outlined below to ensure a smooth development process.

## Table of contents

<!-- * [Live](#live) -->
* [Branching Strategy](#Branch)
* [Development Workflow](#Developer)
* [Code Review Guidelines](#Review)

  
## <a name="Branch"></a>Branching Strategy
<p>We follow a Git branching strategy to manage different aspects of development. Here are the branches:

#### master:
The master branch represents production-ready code. It should always reflect the latest release.

#### dev: 
The dev branch is where features are integrated and tested. Developers should create feature branches from this branch.

#### Feature Branches: 
For every new feature or bug fix, create a feature branch off of develop. Use a descriptive branch name (e.g., feature/new-feature or bugfix/fix-issue).</p>
<pre>
# Example workflow for creating a new feature branch
git checkout dev
git pull origin dev
git checkout -b dev/your_name
</pre>

## <a name="Developer"></a>Development Workflow
**1. Clone the Repository:** Clone the repository to your local machine using the following command:
<pre>
git clone --branch dev https://github.com/qntmnet/Quantumsurvey.git
</pre>

**2. Create a Branch:** Before making any changes, create a new branch for your feature or bug fix. 
<pre>
git checkout -b dev/your_name
</pre>

**3. Make Changes:** Implement the necessary changes in your branch. 

**4. Commit Changes:** Commit your changes with a clear and concise message.
<pre>
git add .
git commit -m "Implement new feature: XYZ"
</pre>

**5. Push Changes:** Push your changes to the remote repository.
<pre>
git push -u origin dev/your_name
</pre>

**Notes :** Follow below steps on GitHub UI.  
**1. Create a Pull Request (PR):** When your feature is ready, create a PR against the dev branch.  
**2. Code Review:** Your code will be reviewed by other team members. Make necessary adjustments if required.  
**3. Merge to Develop:** Once the PR is approved, merge your changes into the develop branch.  
**4. Release Preparation:**  When a set of features is ready for release, create a PR from develop to main and follow the release process.  
#### Note : -
After completion of above mentioned steps, in case you want to continue your work then you are requested to continue your work in dev branch in your local system. 
<pre>
git checkout dev
</pre>
## <a name="Review"></a>Code Review Guidelines

* Ensure your code follows the project's coding standards.   
* Write clear and concise code.  
* Include unit tests for new features or bug fixes.  
* Respond promptly to feedback during code reviews.  
  



