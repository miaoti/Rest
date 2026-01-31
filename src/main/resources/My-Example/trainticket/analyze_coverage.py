#!/usr/bin/env python3
"""
Coverage CD Analysis Script
Analyzes Status Code Coverage (SC) and Status Code Class Coverage (SCC) 
across three testing tools: Your Tool, RESTest, and EvoMaster.
"""

import os
import json
import csv
import glob
import re
from collections import defaultdict
from pathlib import Path

import matplotlib.pyplot as plt
import numpy as np

# Configuration - Data Paths
PATHS = {
    'your_tool': r'c:\Users\Tingshuo_Miao2\Rest\src\main\resources\My-Example\trainticket\allure-results',
    'restest_csv': r'C:\Users\Tingshuo_Miao2\Documents\GitHub\RESTest\target\coverage-data\trainticket',
    'restest_allure': r'C:\Users\Tingshuo_Miao2\Documents\GitHub\RESTest\target\allure-results\trainticket',
    'evomaster': r'C:\Users\Tingshuo_Miao2\Documents\GitHub\EvoMaster\TrainTicket\generated_tests\report.json'
}

# HTTP Status Code Classes
STATUS_CLASSES = {
    '2xx': range(200, 300),
    '3xx': range(300, 400),
    '4xx': range(400, 500),
    '5xx': range(500, 600)
}


def classify_status(code: int) -> str:
    """Classify HTTP status code into class (2xx, 3xx, 4xx, 5xx)."""
    for cls, rng in STATUS_CLASSES.items():
        if code in rng:
            return cls
    return 'other'


def parse_restest_coverage(csv_dir: str, allure_dir: str) -> dict:
    """Parse RESTest coverage CSV files and Allure results."""
    results = {'sc': None, 'scc': None, 'raw_status_codes': set(), 'status_classes': set()}
    
    # Parse CSV for percentage metrics
    csv_files = glob.glob(os.path.join(csv_dir, 'test-coverage-posteriori_*.csv'))
    
    if csv_files:
        csv_file = csv_files[0]
        print(f"[RESTest] Parsing CSV: {csv_file}")
        
        with open(csv_file, 'r', encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                if 'statusCodeCoverage' in row:
                    try:
                        results['sc'] = float(row['statusCodeCoverage'])
                    except ValueError:
                        pass
                if 'statusCodeClassCoverage' in row:
                    try:
                        results['scc'] = float(row['statusCodeClassCoverage'])
                    except ValueError:
                        pass
                break
        
        print(f"[RESTest] CSV Metrics - SC: {results['sc']}%, SCC: {results['scc']}%")
    
    # Parse Allure results for raw status codes
    if os.path.exists(allure_dir):
        result_files = glob.glob(os.path.join(allure_dir, '*-result.json'))
        print(f"[RESTest] Found {len(result_files)} Allure result files")
        
        for result_file in result_files:
            try:
                with open(result_file, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                
                # Check attachments for HTTP status codes
                for att in data.get('attachments', []):
                    att_name = att.get('name', '')
                    # Pattern: "HTTP/1.1 400 " or similar
                    match = re.search(r'HTTP/[\d.]+\s+(\d{3})', att_name)
                    if match:
                        results['raw_status_codes'].add(int(match.group(1)))
                
                # Check steps and their attachments
                for step in data.get('steps', []):
                    for att in step.get('attachments', []):
                        att_name = att.get('name', '')
                        match = re.search(r'HTTP/[\d.]+\s+(\d{3})', att_name)
                        if match:
                            results['raw_status_codes'].add(int(match.group(1)))
                        match = re.search(r'Response\s*\((\d{3})\)', att_name)
                        if match:
                            results['raw_status_codes'].add(int(match.group(1)))
                            
            except (json.JSONDecodeError, IOError):
                continue
        
        # Calculate status classes from raw codes
        for code in results['raw_status_codes']:
            results['status_classes'].add(classify_status(code))
        
        print(f"[RESTest] Covered {len(results['raw_status_codes'])} unique status codes: {sorted(results['raw_status_codes'])}")
        print(f"[RESTest] Covered {len(results['status_classes'])} status classes: {sorted(results['status_classes'])}")
    
    return results


def parse_evomaster_coverage(report_path: str) -> dict:
    """Parse EvoMaster report.json to extract coverage."""
    results = {'sc': None, 'scc': None, 'raw_status_codes': set(), 'status_classes': set()}
    
    if not os.path.exists(report_path):
        print(f"[EvoMaster] Report not found: {report_path}")
        return results
    
    print(f"[EvoMaster] Parsing: {report_path}")
    
    with open(report_path, 'r', encoding='utf-8') as f:
        data = json.load(f)
    
    # Extract covered HTTP status codes
    covered_status = data.get('problemDetails', {}).get('rest', {}).get('coveredHttpStatus', [])
    
    for entry in covered_status:
        for status in entry.get('httpStatus', []):
            results['raw_status_codes'].add(status)
            results['status_classes'].add(classify_status(status))
    
    # Count unique endpoints for reference
    endpoints = data.get('problemDetails', {}).get('rest', {}).get('endpointIds', [])
    
    print(f"[EvoMaster] Covered {len(results['raw_status_codes'])} unique status codes: {sorted(results['raw_status_codes'])}")
    print(f"[EvoMaster] Covered {len(results['status_classes'])} status classes: {sorted(results['status_classes'])}")
    print(f"[EvoMaster] Total endpoints: {len(endpoints)}")
    
    return results


def parse_your_tool_coverage(allure_dir: str) -> dict:
    """Parse Your Tool's Allure result JSON files."""
    results = {'sc': None, 'scc': None, 'raw_status_codes': set(), 'status_classes': set()}
    
    if not os.path.exists(allure_dir):
        print(f"[YourTool] Directory not found: {allure_dir}")
        return results
    
    result_files = glob.glob(os.path.join(allure_dir, '*-result.json'))
    print(f"[YourTool] Found {len(result_files)} result files")
    
    for result_file in result_files:
        try:
            with open(result_file, 'r', encoding='utf-8') as f:
                data = json.load(f)
            
            # Extract status codes from attachments or step names
            # Look for patterns like "Response (200)" or "HTTP/1.1 200"
            
            # Check steps
            for step in data.get('steps', []):
                step_name = step.get('name', '')
                # Pattern: "Response (200)" or similar
                match = re.search(r'Response\s*\((\d{3})\)', step_name)
                if match:
                    results['raw_status_codes'].add(int(match.group(1)))
                
                # Check attachments within step
                for att in step.get('attachments', []):
                    att_name = att.get('name', '')
                    match = re.search(r'Response\s*\((\d{3})\)', att_name)
                    if match:
                        results['raw_status_codes'].add(int(match.group(1)))
                    # Also check for HTTP status in attachment name
                    match = re.search(r'HTTP/[\d.]+\s+(\d{3})', att_name)
                    if match:
                        results['raw_status_codes'].add(int(match.group(1)))
            
            # Check top-level attachments
            for att in data.get('attachments', []):
                att_name = att.get('name', '')
                match = re.search(r'HTTP/[\d.]+\s+(\d{3})', att_name)
                if match:
                    results['raw_status_codes'].add(int(match.group(1)))
                match = re.search(r'Response\s*\((\d{3})\)', att_name)
                if match:
                    results['raw_status_codes'].add(int(match.group(1)))
                    
        except (json.JSONDecodeError, IOError) as e:
            continue
    
    # Calculate status classes
    for code in results['raw_status_codes']:
        results['status_classes'].add(classify_status(code))
    
    print(f"[YourTool] Covered {len(results['raw_status_codes'])} unique status codes: {sorted(results['raw_status_codes'])}")
    print(f"[YourTool] Covered {len(results['status_classes'])} status classes: {sorted(results['status_classes'])}")
    
    return results


def create_comparison_chart(data: dict, output_path: str):
    """Create a comparison bar chart of coverage metrics."""
    tools = list(data.keys())
    
    # Extract metrics
    sc_values = []
    scc_values = []
    sc_count = []
    scc_count = []
    
    for tool in tools:
        d = data[tool]
        # Use percentage if available, otherwise use count
        if d.get('sc') is not None:
            sc_values.append(d['sc'])
        else:
            sc_values.append(len(d.get('raw_status_codes', set())))
        
        if d.get('scc') is not None:
            scc_values.append(d['scc'])
        else:
            scc_values.append(len(d.get('status_classes', set())))
        
        sc_count.append(len(d.get('raw_status_codes', set())))
        scc_count.append(len(d.get('status_classes', set())))
    
    # Create figure with two subplots
    fig, axes = plt.subplots(1, 2, figsize=(14, 6))
    
    x = np.arange(len(tools))
    width = 0.6
    colors = ['#3498db', '#e74c3c', '#2ecc71']
    
    # Status Code Coverage
    ax1 = axes[0]
    bars1 = ax1.bar(x, sc_count, width, color=colors[:len(tools)])
    ax1.set_ylabel('Unique Status Codes Covered')
    ax1.set_title('Status Code Coverage (SC)')
    ax1.set_xticks(x)
    ax1.set_xticklabels(tools)
    ax1.bar_label(bars1, padding=3, fmt='%d')
    
    # Status Code Class Coverage
    ax2 = axes[1]
    bars2 = ax2.bar(x, scc_count, width, color=colors[:len(tools)])
    ax2.set_ylabel('Status Code Classes Covered')
    ax2.set_title('Status Code Class Coverage (SCC)')
    ax2.set_xticks(x)
    ax2.set_xticklabels(tools)
    ax2.bar_label(bars2, padding=3, fmt='%d')
    ax2.set_ylim(0, 6)  # Max 5 classes + buffer
    
    plt.suptitle('Coverage Comparison Across Testing Tools', fontsize=14, fontweight='bold')
    plt.tight_layout()
    plt.savefig(output_path, dpi=150, bbox_inches='tight')
    print(f"\n[Output] Chart saved to: {output_path}")
    plt.close()


def create_cd_diagram(data: dict, output_path: str):
    """Create a Critical Difference style diagram."""
    tools = list(data.keys())
    
    # For CD diagram, we need rankings
    # Rank by number of status codes covered (higher = better = rank 1)
    sc_scores = [(tool, len(data[tool].get('raw_status_codes', set()))) for tool in tools]
    scc_scores = [(tool, len(data[tool].get('status_classes', set()))) for tool in tools]
    
    # Sort by score descending to get ranks
    sc_sorted = sorted(sc_scores, key=lambda x: x[1], reverse=True)
    scc_sorted = sorted(scc_scores, key=lambda x: x[1], reverse=True)
    
    # Assign ranks
    sc_ranks = {tool: rank + 1 for rank, (tool, _) in enumerate(sc_sorted)}
    scc_ranks = {tool: rank + 1 for rank, (tool, _) in enumerate(scc_sorted)}
    
    fig, axes = plt.subplots(2, 1, figsize=(10, 8))
    
    # SC Ranking
    ax1 = axes[0]
    ax1.set_xlim(0.5, len(tools) + 0.5)
    ax1.set_ylim(0, 1)
    ax1.set_title('Status Code Coverage (SC) - Ranking', fontsize=12, fontweight='bold')
    ax1.set_xlabel('Rank (1 = Best)')
    ax1.axhline(y=0.5, color='black', linewidth=2)
    
    colors = {'YourTool': '#3498db', 'RESTest': '#e74c3c', 'EvoMaster': '#2ecc71'}
    
    for tool in tools:
        rank = sc_ranks[tool]
        score = len(data[tool].get('raw_status_codes', set()))
        color = colors.get(tool, 'gray')
        ax1.plot(rank, 0.5, 'o', markersize=15, color=color)
        ax1.text(rank, 0.65, f'{tool}\n({score} codes)', ha='center', fontsize=10)
    
    ax1.set_yticks([])
    ax1.set_xticks(range(1, len(tools) + 1))
    
    # SCC Ranking
    ax2 = axes[1]
    ax2.set_xlim(0.5, len(tools) + 0.5)
    ax2.set_ylim(0, 1)
    ax2.set_title('Status Code Class Coverage (SCC) - Ranking', fontsize=12, fontweight='bold')
    ax2.set_xlabel('Rank (1 = Best)')
    ax2.axhline(y=0.5, color='black', linewidth=2)
    
    for tool in tools:
        rank = scc_ranks[tool]
        score = len(data[tool].get('status_classes', set()))
        color = colors.get(tool, 'gray')
        ax2.plot(rank, 0.5, 'o', markersize=15, color=color)
        ax2.text(rank, 0.65, f'{tool}\n({score} classes)', ha='center', fontsize=10)
    
    ax2.set_yticks([])
    ax2.set_xticks(range(1, len(tools) + 1))
    
    plt.suptitle('Critical Difference Diagram - Coverage Rankings', fontsize=14, fontweight='bold', y=1.02)
    plt.tight_layout()
    plt.savefig(output_path, dpi=150, bbox_inches='tight')
    print(f"[Output] CD Diagram saved to: {output_path}")
    plt.close()


def print_summary(data: dict):
    """Print a summary table of coverage metrics."""
    print("\n" + "="*70)
    print("COVERAGE ANALYSIS SUMMARY")
    print("="*70)
    print(f"{'Tool':<15} {'SC (codes)':<15} {'SCC (classes)':<15} {'Status Codes'}")
    print("-"*70)
    
    for tool, d in data.items():
        sc_count = len(d.get('raw_status_codes', set()))
        scc_count = len(d.get('status_classes', set()))
        codes = sorted(d.get('raw_status_codes', set()))
        codes_str = ', '.join(map(str, codes[:5]))
        if len(codes) > 5:
            codes_str += f'... (+{len(codes)-5} more)'
        print(f"{tool:<15} {sc_count:<15} {scc_count:<15} {codes_str}")
    
    print("="*70)


def main():
    """Main entry point."""
    print("="*70)
    print("COVERAGE CD ANALYSIS")
    print("="*70)
    
    # Parse data from all tools
    results = {}
    
    print("\n--- Parsing RESTest Data ---")
    results['RESTest'] = parse_restest_coverage(PATHS['restest_csv'], PATHS['restest_allure'])
    
    print("\n--- Parsing EvoMaster Data ---")
    results['EvoMaster'] = parse_evomaster_coverage(PATHS['evomaster'])
    
    print("\n--- Parsing Your Tool Data ---")
    results['YourTool'] = parse_your_tool_coverage(PATHS['your_tool'])
    
    # Print summary
    print_summary(results)
    
    # Generate visualizations
    output_dir = os.path.dirname(os.path.abspath(__file__))
    
    print("\n--- Generating Visualizations ---")
    create_comparison_chart(results, os.path.join(output_dir, 'coverage_comparison.png'))
    create_cd_diagram(results, os.path.join(output_dir, 'coverage_cd_diagram.png'))
    
    print("\n[DONE] Analysis complete!")
    return results


if __name__ == '__main__':
    main()
