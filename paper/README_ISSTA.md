# Building the ISSTA 2026 variant of the MIST tool-demo paper

This directory carries two paper variants:

| File | Venue | LaTeX class | Status |
|---|---|---|---|
| `main.tex` | ICSME 2026 Tool Demo | `IEEEtran.cls` (conference) | original; preserve unchanged |
| `main_issta.tex` | ISSTA 2026 Tool Demonstrations (joint with SPLASH 2026) | `acmart.cls` (sigconf,screen,review) | converted from `main.tex` |

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
https://www.ieee.org/conferences/publishing/templates.html. Both files
currently live elsewhere on the build host; the placeholder `IEEEtran.cls`
in this directory is a stub that loads `article` as a fallback for casual
inspection only.

## Build

ICSME variant (5 pages, IEEEtran):

```
pdflatex main.tex && bibtex main && pdflatex main.tex && pdflatex main.tex
```

ISSTA variant (4 pages + 1 page refs, acmart sigconf):

```
pdflatex main_issta.tex && bibtex main_issta && pdflatex main_issta.tex && pdflatex main_issta.tex
```

`latexmk -pdf main_issta.tex` works too once the acmart class is installed.

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
