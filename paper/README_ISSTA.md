# Building the MIST tool-demo paper

> **Active target as of 2026-05-19: ICSE 2027 Tool Demonstration and Data
> Showcase Track** (deadline Oct 23, 2026; 4 pages incl. references;
> `IEEEtran[10pt,conference]`; single-anonymous). The active paper file
> is `main.tex`.
>
> The earlier ISSTA 2026 / ICSME 2026 windows have all closed; see
> the deprecation banner at the top of `main_issta.tex` for details.

This directory carries two paper variants:

| File | Venue | LaTeX class | Status |
|---|---|---|---|
| `main.tex` | **ICSE 2027 Tool Demonstration and Data Showcase (active)** | `IEEEtran` (10pt, conference) | retargeted from ICSME 2026 on 2026-05-19 |
| `main_issta.tex` | ISSTA 2026 Tool Demonstrations (window closed) | `acmart` (sigconf,screen,review) | deprecated; fallback if ISSTA 2027 reopens an acmart window |

## Prerequisites

The ISSTA variant uses ACM's `acmart` class, which is **not** committed to this
directory. Fetch the latest release before building:

* Download: https://www.acm.org/publications/proceedings-template (the
  "Primary Article Template" tarball) **or** from CTAN:
  https://ctan.org/pkg/acmart
* Drop the tarball's contents (`acmart.cls`, `acmart.bst` if you want,
  `ACM-Reference-Format.bst`, `sample-*` examples) somewhere on your TeX
  search path, or unpack into `paper/` and let `latexmk` find them locally.
* The same tarball ships `ACM-Reference-Format.bst`, which `main_issta.tex`
  uses via `\bibliographystyle{ACM-Reference-Format}`.

The ICSME variant (`main.tex`) needs `IEEEtran.cls` and `IEEEtran.bst` from
https://www.ieee.org/conferences/publishing/templates.html or CTAN
(https://ctan.org/pkg/ieeetran). Do **not** drop a stub `IEEEtran.cls` into
this directory: the local copy is searched before system-wide TeX paths,
so a stub will mask the real class and the build will fail at line 2
(`\IEEEoverridecommandlockouts`). Overleaf, MiKTeX (with auto-install),
and full TeX Live all ship `IEEEtran.cls`; leave them to supply it.

## Build

ICSE 2027 variant (4 pages incl. refs, IEEEtran 10pt conference):

```
pdflatex main.tex && bibtex main && pdflatex main.tex && pdflatex main.tex
```

ISSTA variant (deprecated; only resurrect if a future ISSTA reopens):

```
pdflatex main_issta.tex && bibtex main_issta && pdflatex main_issta.tex && pdflatex main_issta.tex
```

## ISSTA-specific notes

* Track: ISSTA 2026 Tool Demonstrations, joint with SPLASH 2026.
* Submission deadline: 26 June 2026, Anywhere on Earth.
* Length: 4 pages of body + up to 1 page of references (5 pages total).
* Review model: **single-blind** (authors visible at submission). Do not
  anonymise the paper, the repository URL, or any artifact link.
* The `review` option in `\documentclass` enables line numbers for
  reviewers; keep it on for the submitted PDF and turn it off for
  camera-ready.
* `\setcopyright{rightsretained}` is a safe default; re-check against the
  ISSTA 2026 author kit before camera-ready (the value can change to
  `acmlicensed`, `acmcopyright`, `cc-by`, etc., depending on the rights
  agreement you sign).
* The `cmISBN`, `cmDOI`, and Zenodo DOI placeholders are flagged as
  `	odo{...}` in `main_issta.tex`; fill them at camera-ready.

## Diff between ICSME and ISSTA variants

Tracked in `NOTES_TO_AUTHOR.md` under "ISSTA 2026 conversion". Summary:
* Document class swap (`IEEEtran[conference]` -> `acmart[sigconf,screen,review]`).
* Author block rewritten using `uthor/ffiliation/\email` instead of
  `\IEEEauthorblockN/A`.
* ACM CCS concepts + keywords + conference metadata added.
* `\setcopyright`, `cmConference`, `cmBooktitle`, `cmISBN`,
  `cmDOI`, `cmPrice`, `cmYear`, `\copyrightyear` added.
* Bibliography style changed from `IEEEtran` to `ACM-Reference-Format`.
* New section: Tool Availability (mandatory for ISSTA Tool Demos).
* Compression: §3 final "End-to-end flow" paragraph dropped (redundant
  with Figure 1), Related Work first paragraph compressed to one
  sentence with two named comparisons (EvoMaster, RESTest) plus a
  parenthetical citation block, Figure 2 caption shortened by one
  sentence, Algorithm 1 lines 6-7 merged.
* Identical between variants: abstract, motivating example, architecture
  description, contributions, case study, fault table, concrete numbers
  (37, 265, 2733, 20h, 12h), interlock sentence in section 4.
