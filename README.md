# GITApi
This project delivers some method to to push file in git automatically
This project implements following HTTP request in java 
This HTTP Methods are taken from: https://www.youtube.com/watch?v=3VCN18PMVnw 
1. GET /repos/:user/:repo/git/refs/heads/master
Save sha-latest-commit

2. GET /repos/:user/:repo/git/commits/{{sha-latest-commit}}
Save sha-base-tree

3. POST /repos/:user/:repo/git/trees
{
  "base_tree": "{{sha-base-tree}}",
  "tree": [
    {
      "path": "NewFile1.txt",
      "mode": "100644",
      "type": "blob",
      "content": "This is NewFile1."
    },
    {
      "path": "NewFile2.txt",
      "mode": "100644",
      "type": "blob",
      "content": "This is NewFile2."
    }
  ]
}

Save sha-new-tree

4. POST /repos/:user/:repo/git/commits
{
  "parents": ["{{sha-latest-commit}}"],
  "tree": "{{sha-new-tree}}",
  "message": ""
}

Save sha-new-commit

5. POST /repos/:user/:repo/git/refs/heads/master
{
  "sha": "{{sha-new-commit}}"
}
