package com.mademil.ferramentaria.controllers;

import com.mademil.ferramentaria.entities.Chuck;
import com.mademil.ferramentaria.entities.Tool;
import com.mademil.ferramentaria.entities.Template;
import com.mademil.ferramentaria.entities.Vise;
import com.mademil.ferramentaria.entities.User;
import com.mademil.ferramentaria.repositories.UserRepository;
import com.mademil.ferramentaria.service.ChuckService;
import com.mademil.ferramentaria.service.ToolService;
import com.mademil.ferramentaria.service.TemplateService;
import com.mademil.ferramentaria.service.ViseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class EntityController {
    @Autowired
    private ChuckService chuckService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ViseService viseService;

    @Autowired
    UserRepository userRepository;

    // Disallow images to prevent @ModelAttribute from
    // auto binding the image file to the image field on each entity.
    // Needs to exist because multipart must be converted to byte[] before saving.
    @InitBinder
    public void initBinder(WebDataBinder binder) { 
        binder.setDisallowedFields("chuckImage", "toolImage", "templateImage", "viseImage"); 
    }

    @GetMapping("/entidades")
    public String serveEntitiesPage(
        @RequestParam(value = "error", required = false) String error,
        @AuthenticationPrincipal UserDetails springUser,
        Model model){

        User user = userRepository.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (error != null) {
            model.addAttribute("error", error);
        }

        List<Chuck> chucks = chuckService.getAllActiveChucks();
        List<Tool> tools = toolService.getAllActiveTools();
        List<Template> templates = templateService.getAllActiveTemplates();
        List<Vise> vises = viseService.getAllActiveVises();

        model.addAttribute("user", user);
        model.addAttribute("chucks", chucks);
        model.addAttribute("tools", tools);
        model.addAttribute("templates", templates);
        model.addAttribute("vises", vises);

        return "entities";
    }

    @GetMapping("/formulario-castanha")
    public String addChuckForm(
        @RequestParam(value = "chuckId", required = false) Integer chuckId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }

        if (chuckId != null) {
            Optional<Chuck> optionalChuck = chuckService.getChuckById(chuckId);

            if (optionalChuck.isPresent()) {
                Chuck chuck = optionalChuck.get();
                model.addAttribute("chuck", chuck);
            }else{
                model.addAttribute("error", "Castanha não encontrada: " + chuckId);
            }
        }
        return "form-chuck";
    }

    @PostMapping("/adicionar-castanha")
    public String addChuckToDatabase(
        @RequestParam(value = "chuckId", required = false) Integer chuckId,
        @RequestParam("chuckImage") MultipartFile chuckImage, // Handle image manually because it was disallowed on .initBinder()
        @Valid @ModelAttribute Chuck chuck,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {

        // If there are validation errors, return to the form with the error message
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/formulario-castanha";
        }
    
        try {
            if (chuckId != null) {
                chuck.setChuckId(chuckId);
            }
            chuck.setChuckName(chuck.getChuckName().toUpperCase());
            chuck.setChuckLocation(chuck.getChuckLocation().toUpperCase());
            chuck.setChuckImage(chuckImage.getBytes());
            chuck.setIsDeleted(false);  // It is default false in DB but does not hurt to make sure
            chuckService.saveChuck(chuck);
    
            return "redirect:/entidades";
        } catch (DataIntegrityViolationException e) {
            System.err.println("Data Integrity error: " + e.getMessage());

            // This error message is generic because error handling is done in the entity class via constraint annotations
            redirectAttributes.addFlashAttribute("error", "Integridade de dados violada"); 
            return "redirect:/formulario-castanha";
        } catch (IOException e) {
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-castanha";
        }
    }

    @GetMapping("/formulario-ferramenta")
    public String addToolForm(
        @RequestParam(value = "toolId", required = false) Integer toolId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){
            
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
            
        if (toolId != null) {
            Optional<Tool> optionalTool = toolService.getToolById(toolId);

            if (optionalTool.isPresent()) {
                Tool tool = optionalTool.get();
                model.addAttribute("tool", tool);
            }else{
                model.addAttribute("error", "Ferramenta não encontrada: " + toolId);
            }
        }
        return "form-tool";
    }

    @PostMapping("/adicionar-ferramenta")
    public String addToolToDatabase(
        @RequestParam(value = "toolId", required = false) Integer toolId,
        @RequestParam("toolImage") MultipartFile toolImage,
        @Valid @ModelAttribute Tool tool,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/formulario-ferramenta";
        }

        try {
            if (toolId != null) {
                tool.setToolId(toolId);
            }
            tool.setToolName(tool.getToolName().toUpperCase());
            tool.setToolImage(toolImage.getBytes());
            tool.setIsDeleted(false);
            toolService.saveTool(tool);

            return "redirect:/entidades";
        }catch (DataIntegrityViolationException e){
            System.err.println("Data Integrity error " + e.getMostSpecificCause());
            redirectAttributes.addAttribute("error", "Integridade de dados violada");
            return "redirect:/formulario-ferramenta";
        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-ferramenta";
        }
    }

    @GetMapping("/formulario-gabarito")
    public String addTemplateForm(
        @RequestParam(value = "templateId", required = false) Integer templateId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){

        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }

        if (templateId != null) {
            Optional<Template> optionalTemplate = templateService.getTemplateById(templateId);

            if (optionalTemplate.isPresent()) {
                Template template = optionalTemplate.get();
                model.addAttribute("template", template);
            }else{
                model.addAttribute("error", "Gabarito não encontrado: " + templateId);
            }
        }
        return "form-template";
    }

    @PostMapping("/adicionar-gabarito")
    public String addTemplateToDatabase(
        @RequestParam(value = "templateId", required = false) Integer templateId,
        @RequestParam("templateImage") MultipartFile templateImage,
        @Valid @ModelAttribute Template template,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/formulario-ferramenta";
        }

        try {
            if (templateId != null) {
                template.setTemplateId(templateId);
            }
            template.setTemplateName(template.getTemplateName().toUpperCase());
            template.setTemplateLocation(template.getTemplateLocation().toUpperCase());
            template.setTemplateImage(templateImage.getBytes());
            template.setIsDeleted(false);
            templateService.saveTemplate(template);
            
            return "redirect:/entidades";
        }catch (DataIntegrityViolationException e){
            System.err.println("Data Integrity error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Integridade de dados violada");
            return "redirect:/formulario-gabarito";
        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-gabarito";
        }
    }

    @GetMapping("/formulario-morsa")
    public String addViseForm(
        @RequestParam(value = "viseId", required = false) Integer viseId,
        @RequestParam(value = "error", required = false) String errorMessage,
        Model model){
            
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
        
        if (viseId != null) {
            Optional<Vise> optionalVise = viseService.getViseById(viseId);

            if (optionalVise.isPresent()) {
                Vise vise = optionalVise.get();
                model.addAttribute("vise", vise);
            }else{
                model.addAttribute("error", "Morsa não encontrado: " + viseId);
            }
        }
        return "form-vise";
    }

    @PostMapping("/adicionar-morsa")
    public String addViseToDatabase(
        @RequestParam(value = "viseId", required = false) Integer viseId,
        @RequestParam("viseImage") MultipartFile viseImage,
        @Valid @ModelAttribute Vise vise,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);
            return "redirect:/formulario-ferramenta";
        }

        try {
            if (viseId != null) {
                vise.setViseId(viseId);
            }
            vise.setViseName(vise.getViseName().toUpperCase());
            vise.setViseLocation(vise.getViseLocation().toUpperCase());
            vise.setViseImage(viseImage.getBytes());
            vise.setIsDeleted(false);
            viseService.saveVise(vise);

            return "redirect:/entidades";
        }catch (DataIntegrityViolationException e){
            System.err.println("Data Integrity error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Integridade de dados violada");
            return "redirect:/formulario-morsa";
        }catch (IOException e){
            System.err.println("IO Error: " + e.getMessage());
            redirectAttributes.addAttribute("error", "Não foi possível ler a imagem");
            return "redirect:/formulario-morsa";
        }
    }

    @PostMapping("/inativar-entidade")
    public String deactivateEntity(
        @AuthenticationPrincipal UserDetails springUser,
        @RequestParam("entityIdentifier") String entityIdentifier,
        @RequestParam(value = "chuckId", required = false) Integer chuckId,
        @RequestParam(value = "toolId", required = false) Integer toolId,
        @RequestParam(value = "templateId", required = false) Integer templateId,
        @RequestParam(value = "viseId",required = false) Integer viseId,
        RedirectAttributes redirectAttributes){

        User user = userRepository.findByUsername(springUser.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
            
        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            redirectAttributes.addAttribute("error", "Usuário sem permissão");
            return "redirect:/entidades";
        }

        switch (entityIdentifier.toUpperCase()) {
            case "CHUCK":
                if (chuckId != null) {
                    Optional<Chuck> optionalChuck = chuckService.getChuckById(chuckId);

                    if (optionalChuck.isPresent()) {
                        Chuck chuck = optionalChuck.get();
                        chuck.setIsDeleted(true);
                        chuckService.saveChuck(chuck);
                    }else{
                        redirectAttributes.addAttribute("error", "Erro ao inativar castanha: castanha com o ID "+ chuckId + "não existe");
                        return "redirect:/entidades";
                    }
                }else{
                    redirectAttributes.addAttribute("error", "Erro ao inativar castanha: nenhum ID foi providenciado");
                    return "redirect:/entidades";
                }

                break;

            case "TOOL":
                if (toolId != null) {
                    Optional<Tool> optionalTool = toolService.getToolById(toolId);
                    
                    if (optionalTool.isPresent()) {
                        Tool tool = optionalTool.get();
                        tool.setIsDeleted(true);
                        toolService.saveTool(tool);
                    }else{
                        redirectAttributes.addAttribute("error", "Erro ao inativar ferramenta: ferramenta com o ID "+ toolId + "não existe");
                        return "redirect:/entidades";
                    }
                }else{
                    redirectAttributes.addAttribute("error", "Erro ao inativar Ferramenta: nenhum ID foi providenciado");
                    return "redirect:/entidades";
                }

                break;

            case "TEMPLATE":
                if (templateId != null) {
                    Optional<Template> optionalTemplate = templateService.getTemplateById(templateId);

                    if (optionalTemplate.isPresent()) {
                        Template template = optionalTemplate.get();
                        template.setIsDeleted(true);
                        templateService.saveTemplate(template);
                    }else{
                        redirectAttributes.addAttribute("error", "Erro ao inativar gabarito: gabarito com o ID "+ templateId + "não existe");
                        return "redirect:/entidades";
                    }
                }else{
                    redirectAttributes.addAttribute("error", "Erro ao inativar gabarito: nenhum ID foi providenciado");
                    return "redirect:/entidades";
                }

                break;

            case "VISE":
                if (viseId != null) {
                    Optional<Vise> optionalVise = viseService.getViseById(viseId);

                    if (optionalVise.isPresent()) {
                        Vise vise = optionalVise.get();
                        vise.setIsDeleted(true);
                        viseService.saveVise(vise);
                    }else{
                        redirectAttributes.addAttribute("error", "Erro ao inativar morsa: morsa com o ID "+ viseId + "não existe");
                        return "redirect:/entidades";
                    }
                }else{
                    redirectAttributes.addAttribute("error", "Erro ao inativar morsa: nenhum ID foi providenciado");
                    return "redirect:/entidades";
                }

                break;
        
            default:
                redirectAttributes.addAttribute("error", "Erro inesperado");
                return "redirect:/entidades";
        }
        return "redirect:/entidades";
    }
}
