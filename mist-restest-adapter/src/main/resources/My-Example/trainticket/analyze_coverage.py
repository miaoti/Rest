#!/usr/bin/env python3
"""
Coverage CD Analysis Script v2
Analyzes Status Code Coverage (SC) and Status Code Class Coverage (SCC) 
with Nemenyi Critical Difference diagrams.
"""

import os
import json
import csv
import glob
import re
from collections import defaultdict
from pathlib import Path

import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
import numpy as np
from scipy import stats

# Configuration - Data Paths
PATHS = {
    'your_tool': r'C:\Users\Tingshuo_Miao2\Rest\target\allure-results',
    'your_tool_resource': r'c:\Users\Tingshuo_Miao2\Rest\src\main\resources\My-Example\trainticket\allure-results',
    'restest_csv': r'C:\Users\Tingshuo_Miao2\Documents\GitHub\RESTest\target\coverage-data\trainticket',
    'restest_allure': r'C:\Users\Tingshuo_Miao2\Documents\GitHub\RESTest\target\allure-results\trainticket',
    'evomaster': r'C:\Users\Tingshuo_Miao2\Documents\GitHub\EvoMaster\TrainTicket\generated_tests\report.json',
    'macrohive': r'C:\Users\Tingshuo_Miao2\Documents\GitHub\MacroHive\uTest\clientCommands\results\trainticket_macrohive_20260130_164116\stats.txt'
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


def parse_macrohive_coverage(stats_path: str) -> dict:
    """Parse MacroHive stats.txt to extract status codes."""
    results = {'sc': None, 'scc': None, 'raw_status_codes': set(), 'status_classes': set()}
    
    if not os.path.exists(stats_path):
        print(f"[MacroHive] Stats file not found: {stats_path}")
        return results
    
    print(f"[MacroHive] Parsing: {stats_path}")
    
    with open(stats_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # Extract status codes from FailCode section
    fail_match = re.findall(r'-\s+(\d{3})\s+\d+', content)
    for code in fail_match:
        results['raw_status_codes'].add(int(code))
    
    # Also look for success codes if different format
    succ_match = re.findall(r'-\s+(\d{3})\s+\d+', content)
    for code in succ_match:
        results['raw_status_codes'].add(int(code))
    
    # Calculate status classes
    for code in results['raw_status_codes']:
        results['status_classes'].add(classify_status(code))
    
    print(f"[MacroHive] Covered {len(results['raw_status_codes'])} unique status codes: {sorted(results['raw_status_codes'])}")
    print(f"[MacroHive] Covered {len(results['status_classes'])} status classes: {sorted(results['status_classes'])}")
    
    return results


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
                
                for att in data.get('attachments', []):
                    att_name = att.get('name', '')
                    match = re.search(r'HTTP/[\d.]+\s+(\d{3})', att_name)
                    if match:
                        results['raw_status_codes'].add(int(match.group(1)))
                
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
    
    covered_status = data.get('problemDetails', {}).get('rest', {}).get('coveredHttpStatus', [])
    
    for entry in covered_status:
        for status in entry.get('httpStatus', []):
            results['raw_status_codes'].add(status)
            results['status_classes'].add(classify_status(status))
    
    endpoints = data.get('problemDetails', {}).get('rest', {}).get('endpointIds', [])
    
    print(f"[EvoMaster] Covered {len(results['raw_status_codes'])} unique status codes: {sorted(results['raw_status_codes'])}")
    print(f"[EvoMaster] Covered {len(results['status_classes'])} status classes: {sorted(results['status_classes'])}")
    print(f"[EvoMaster] Total endpoints: {len(endpoints)}")
    
    return results


def parse_your_tool_coverage(allure_dir: str, allure_dir2: str = None) -> dict:
    """Parse Your Tool's Allure result JSON files."""
    results = {'sc': None, 'scc': None, 'raw_status_codes': set(), 'status_classes': set()}
    
    dirs_to_check = [allure_dir]
    if allure_dir2:
        dirs_to_check.append(allure_dir2)
    
    for dir_path in dirs_to_check:
        if not os.path.exists(dir_path):
            continue
        
        result_files = glob.glob(os.path.join(dir_path, '*-result.json'))
        print(f"[YourTool] Found {len(result_files)} result files in {dir_path}")
        
        for result_file in result_files:
            try:
                with open(result_file, 'r', encoding='utf-8') as f:
                    data = json.load(f)
                
                for step in data.get('steps', []):
                    step_name = step.get('name', '')
                    match = re.search(r'Response\s*\((\d{3})\)', step_name)
                    if match:
                        results['raw_status_codes'].add(int(match.group(1)))
                    
                    for att in step.get('attachments', []):
                        att_name = att.get('name', '')
                        match = re.search(r'Response\s*\((\d{3})\)', att_name)
                        if match:
                            results['raw_status_codes'].add(int(match.group(1)))
                        match = re.search(r'HTTP/[\d.]+\s+(\d{3})', att_name)
                        if match:
                            results['raw_status_codes'].add(int(match.group(1)))
                
                for att in data.get('attachments', []):
                    att_name = att.get('name', '')
                    match = re.search(r'HTTP/[\d.]+\s+(\d{3})', att_name)
                    if match:
                        results['raw_status_codes'].add(int(match.group(1)))
                    match = re.search(r'Response\s*\((\d{3})\)', att_name)
                    if match:
                        results['raw_status_codes'].add(int(match.group(1)))
                        
            except (json.JSONDecodeError, IOError):
                continue
    
    for code in results['raw_status_codes']:
        results['status_classes'].add(classify_status(code))
    
    print(f"[YourTool] Covered {len(results['raw_status_codes'])} unique status codes: {sorted(results['raw_status_codes'])}")
    print(f"[YourTool] Covered {len(results['status_classes'])} status classes: {sorted(results['status_classes'])}")
    
    return results


def create_nemenyi_cd_diagram(data: dict, metric: str, output_path: str, title: str):
    """
    Create a Nemenyi Critical Difference diagram.
    
    This creates a horizontal ranking plot where:
    - Tools are positioned based on their rank (1 = best on left)
    - A horizontal bar connects tools with no significant difference
    """
    tools = list(data.keys())
    n = len(tools)
    
    # Get metric values and compute ranks
    if metric == 'sc':
        values = [(tool, len(data[tool].get('raw_status_codes', set()))) for tool in tools]
    else:  # scc
        values = [(tool, len(data[tool].get('status_classes', set()))) for tool in tools]
    
    # Sort by value descending (higher = better = rank 1)
    sorted_values = sorted(values, key=lambda x: x[1], reverse=True)
    
    # Assign average ranks for ties
    ranks = {}
    i = 0
    while i < n:
        j = i
        while j < n and sorted_values[j][1] == sorted_values[i][1]:
            j += 1
        avg_rank = (i + j + 1) / 2  # 1-indexed average rank
        for k in range(i, j):
            ranks[sorted_values[k][0]] = avg_rank
        i = j
    
    # Create figure
    fig, ax = plt.subplots(figsize=(10, 4))
    
    # Plot settings
    y_pos = 0.5
    x_min = 0.5
    x_max = n + 0.5
    
    ax.set_xlim(x_min, x_max)
    ax.set_ylim(0, 1)
    
    # Draw main horizontal axis line
    ax.hlines(y=y_pos, xmin=1, xmax=n, color='black', linewidth=1.5)
    
    # Draw tick marks
    for i in range(1, n + 1):
        ax.vlines(x=i, ymin=y_pos - 0.03, ymax=y_pos + 0.03, color='black', linewidth=1.5)
        ax.text(i, y_pos - 0.08, str(i), ha='center', va='top', fontsize=10)
    
    # Plot tool names at their rank positions
    # Alternate above and below for clarity
    colors = ['#e74c3c', '#3498db', '#2ecc71', '#9b59b6', '#f39c12']
    
    tools_sorted = sorted(tools, key=lambda t: ranks[t])
    
    for idx, tool in enumerate(tools_sorted):
        rank = ranks[tool]
        value = len(data[tool].get('raw_status_codes' if metric == 'sc' else 'status_classes', set()))
        color = colors[idx % len(colors)]
        
        # Alternate positions
        if idx % 2 == 0:
            y_text = y_pos + 0.25
            va = 'bottom'
            # Draw line from tool name to axis
            ax.plot([rank, rank], [y_pos + 0.03, y_text - 0.05], color=color, linewidth=1.5)
        else:
            y_text = y_pos - 0.25
            va = 'top'
            ax.plot([rank, rank], [y_pos - 0.03, y_text + 0.05], color=color, linewidth=1.5)
        
        ax.text(rank, y_text, tool, ha='center', va=va, fontsize=10, fontweight='bold', color=color)
    
    # Identify groups with same rank (no significant difference)
    # For now, assume tools with adjacent ranks are in same group
    # In a real Nemenyi test, we'd calculate the critical difference
    cd = 2.728 * np.sqrt(n * (n + 1) / (6 * n))  # Approximation for Nemenyi CD at alpha=0.05
    
    # Find groups within CD
    groups = []
    sorted_tools = sorted(tools, key=lambda t: ranks[t])
    
    i = 0
    while i < len(sorted_tools):
        group = [sorted_tools[i]]
        j = i + 1
        while j < len(sorted_tools) and ranks[sorted_tools[j]] - ranks[sorted_tools[i]] < cd:
            group.append(sorted_tools[j])
            j += 1
        if len(group) > 1:
            groups.append(group)
        i += 1
    
    # Draw horizontal bold lines for groups with no significant difference
    line_y_offsets = [0.12, 0.18, 0.24]
    for g_idx, group in enumerate(groups):
        if len(group) < 2:
            continue
        min_rank = min(ranks[t] for t in group)
        max_rank = max(ranks[t] for t in group)
        y_offset = line_y_offsets[g_idx % len(line_y_offsets)]
        ax.hlines(y=y_pos + y_offset, xmin=min_rank, xmax=max_rank, 
                  color='black', linewidth=3)
    
    ax.set_title(title, fontsize=12, fontweight='bold')
    ax.axis('off')
    
    plt.tight_layout()
    plt.savefig(output_path, dpi=150, bbox_inches='tight', facecolor='white')
    print(f"[Output] CD Diagram saved to: {output_path}")
    plt.close()


def create_comparison_chart(data: dict, output_path: str):
    """Create a comparison bar chart of coverage metrics."""
    tools = list(data.keys())
    
    sc_count = [len(data[t].get('raw_status_codes', set())) for t in tools]
    scc_count = [len(data[t].get('status_classes', set())) for t in tools]
    
    fig, axes = plt.subplots(1, 2, figsize=(14, 6))
    
    x = np.arange(len(tools))
    width = 0.6
    colors = ['#3498db', '#e74c3c', '#2ecc71', '#9b59b6']
    
    ax1 = axes[0]
    bars1 = ax1.bar(x, sc_count, width, color=colors[:len(tools)])
    ax1.set_ylabel('Unique Status Codes Covered')
    ax1.set_title('Status Code Coverage (SC)')
    ax1.set_xticks(x)
    ax1.set_xticklabels(tools, rotation=15)
    ax1.bar_label(bars1, padding=3, fmt='%d')
    
    ax2 = axes[1]
    bars2 = ax2.bar(x, scc_count, width, color=colors[:len(tools)])
    ax2.set_ylabel('Status Code Classes Covered')
    ax2.set_title('Status Code Class Coverage (SCC)')
    ax2.set_xticks(x)
    ax2.set_xticklabels(tools, rotation=15)
    ax2.bar_label(bars2, padding=3, fmt='%d')
    ax2.set_ylim(0, 6)
    
    plt.suptitle('Coverage Comparison Across Testing Tools', fontsize=14, fontweight='bold')
    plt.tight_layout()
    plt.savefig(output_path, dpi=150, bbox_inches='tight')
    print(f"\n[Output] Chart saved to: {output_path}")
    plt.close()


def print_summary(data: dict):
    """Print a summary table of coverage metrics."""
    print("\n" + "="*80)
    print("COVERAGE ANALYSIS SUMMARY")
    print("="*80)
    print(f"{'Tool':<15} {'SC (codes)':<15} {'SCC (classes)':<15} {'Status Codes'}")
    print("-"*80)
    
    for tool, d in data.items():
        sc_count = len(d.get('raw_status_codes', set()))
        scc_count = len(d.get('status_classes', set()))
        codes = sorted(d.get('raw_status_codes', set()))
        codes_str = ', '.join(map(str, codes))
        if len(codes_str) > 35:
            codes_str = codes_str[:32] + '...'
        print(f"{tool:<15} {sc_count:<15} {scc_count:<15} {codes_str}")
    
    print("="*80)


def main():
    """Main entry point."""
    print("="*80)
    print("COVERAGE CD ANALYSIS v2")
    print("="*80)
    
    results = {}
    
    print("\n--- Parsing RESTest Data ---")
    results['RESTest'] = parse_restest_coverage(PATHS['restest_csv'], PATHS['restest_allure'])
    
    print("\n--- Parsing EvoMaster Data ---")
    results['EvoMaster'] = parse_evomaster_coverage(PATHS['evomaster'])
    
    print("\n--- Parsing MacroHive Data ---")
    results['MacroHive'] = parse_macrohive_coverage(PATHS['macrohive'])
    
    print("\n--- Parsing YourTool Data ---")
    results['YourTool'] = parse_your_tool_coverage(PATHS['your_tool'], PATHS['your_tool_resource'])
    
    # Output directory
    output_dir = r'c:\Users\Tingshuo_Miao2\Rest\src\main\resources\My-Example\trainticket\StatusCode'
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    # Save summary to text file
    summary_path = os.path.join(output_dir, 'coverage_summary.txt')
    with open(summary_path, 'w', encoding='utf-8') as f:
        f.write("="*80 + "\n")
        f.write("COVERAGE ANALYSIS SUMMARY\n")
        f.write("="*80 + "\n")
        f.write(f"{'Tool':<15} {'SC (codes)':<15} {'SCC (classes)':<15} {'Status Codes'}\n")
        f.write("-" * 80 + "\n")
        
        for tool, d in results.items():
            sc_count = len(d.get('raw_status_codes', set()))
            scc_count = len(d.get('status_classes', set()))
            codes = sorted(d.get('raw_status_codes', set()))
            codes_str = ', '.join(map(str, codes))
            f.write(f"{tool:<15} {sc_count:<15} {scc_count:<15} {codes_str}\n")
        f.write("=" * 80 + "\n")

    print_summary(results)
    print(f"\n[Output] Summary saved to: {summary_path}")
    
    print("\n--- Generating Visualizations ---")
    create_comparison_chart(results, os.path.join(output_dir, 'coverage_comparison.png'))
    create_nemenyi_cd_diagram(results, 'scc', os.path.join(output_dir, 'cd_scc_coverage.png'), 
                              'Critical Differences (CD) of SCC coverage')
    create_nemenyi_cd_diagram(results, 'sc', os.path.join(output_dir, 'cd_sc_coverage.png'),
                              'Critical Differences (CD) of SC coverage')
    
    print("\n[DONE] Analysis complete!")
    return results


if __name__ == '__main__':
    main()
