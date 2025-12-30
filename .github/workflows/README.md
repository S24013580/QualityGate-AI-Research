# CI/CD Workflow for Research Project

## Overview

This CI/CD workflow demonstrates how the **separated projects** work together:

1. **Research Project** (this repo) - Contains Java code to test
2. **QualityGate-AI Tool** (separate repo) - Generates tests

## How It Works

### Workflow Steps

1. **Checkout Research Project** - Gets the Java code to test
2. **Checkout QualityGate-AI Tool** - Gets the test generation tool
3. **Setup Environments** - Java 11 and Python 3.9
4. **Install Dependencies** - Python packages and Ollama
5. **Generate Tests** - Tool reads Java files and generates tests
6. **Compile & Test** - Research project compiles and runs tests
7. **Generate Reports** - Coverage and mutation testing
8. **Evaluate** - Tool evaluates the generated tests
9. **Upload Artifacts** - Reports and generated tests

### Key Points

- **Two separate repositories** are checked out
- **Tool generates tests** for the research project
- **Research project** compiles and runs the tests
- **No code sharing** - only file I/O between projects

## Setup Instructions

### Option 1: Public Tool Repository

If QualityGate-AI tool is in a public repository:

1. Update the workflow file (`.github/workflows/ci.yml`):
   ```yaml
   repository: 'your-org/QualityGate-AI'  # Your tool repo
   ```

2. Remove the `token` line (not needed for public repos)

### Option 2: Private Tool Repository

If QualityGate-AI tool is in a private repository:

1. Create a GitHub Personal Access Token with `repo` scope
2. Add it as a secret in this repository:
   - Go to Settings → Secrets and variables → Actions
   - Add secret: `QUALITYGATE_AI_TOKEN`
3. Update the workflow file:
   ```yaml
   repository: 'your-org/QualityGate-AI'
   token: ${{ secrets.QUALITYGATE_AI_TOKEN }}
   ```

### Option 3: Tool in Same Organization

If both repos are in the same GitHub organization:

```yaml
repository: ${{ github.repository_owner }}/QualityGate-AI
```

## Alternative: Single Repository Approach

If you want both projects in one repository:

```yaml
- name: Checkout code
  uses: actions/checkout@v4

# Then use paths directly:
- name: Generate tests
  run: |
    python -m src.cli generate \
      --module ./research-project/src/main/java/... \
      --output ./research-project/src/test/java/...
```

## Workflow Triggers

The workflow runs on:
- **Push** to main/master/develop branches
- **Pull requests** to main/master/develop
- **Manual trigger** (workflow_dispatch)

## Artifacts Generated

After each run, you can download:
- **Coverage reports** (JaCoCo HTML)
- **Mutation reports** (PIT HTML)
- **Evaluation reports** (HTML)
- **Generated test files** (Java)

## Viewing Results

1. Go to **Actions** tab in GitHub
2. Click on the workflow run
3. Scroll to **Artifacts** section
4. Download and view reports

## Troubleshooting

### Issue: "Repository not found"
- Check repository name is correct
- Verify token has access (for private repos)
- Ensure repository exists

### Issue: "Tests don't compile"
- Check generated tests are in correct package
- Verify imports match project structure
- Review compilation errors in logs

### Issue: "Ollama timeout"
- Test generation can take 10-15 minutes
- Increase timeout in workflow if needed
- Check Ollama service started correctly

## Customization

Edit `.github/workflows/ci.yml` to:
- Change Java/Python versions
- Adjust timeouts
- Add notifications
- Modify quality gates
- Add additional steps

