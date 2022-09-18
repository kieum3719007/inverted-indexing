package com.example.demo.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.service.DocumentService;
import com.example.demo.service.IndexService;
import com.example.demo.service.SearchService;

@Controller
public final class HomeController {

	private final SearchService searchService;
	private final IndexService service;
	private final DocumentService documentService;

	public HomeController(
			IndexService service,
			DocumentService documentService,
			SearchService searchService
			) {
		this.searchService = searchService;
		this.service = service;
		this.documentService = documentService;
	}

	@GetMapping(value = { "/", "/index" })
	public ModelAndView index() {
		return new ModelAndView("home");
	}

	@PostMapping("/search")
	public ModelAndView search(String searchText) throws IOException {
		final List<String> searchResult = searchService.search(searchText);
		final ModelAndView modelAndView = new ModelAndView("home");

		modelAndView
		.addObject("documents", searchResult)
		.addObject("search-text", searchText);

		return modelAndView;
	}

	@PostMapping("/process-file")
	public ModelAndView submit(@RequestParam List<CommonsMultipartFile> files) {
		files.stream()
		.filter(item -> "txt".equals(FilenameUtils.getExtension(item.getOriginalFilename())))
		.forEach(item -> {
			try {
				service.indexDocument(item);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
		service.updateDF();

		final ModelAndView modelAndView = new ModelAndView("result");
		modelAndView.addObject("documents", documentService.getAll());
		return modelAndView;
	}
}
